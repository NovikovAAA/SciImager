package com.visualipcv.controller;

import com.visualipcv.controller.binding.PropertyChangedEventListener;
import com.visualipcv.controller.binding.UIProperty;
import com.visualipcv.core.Connection;
import com.visualipcv.core.Graph;
import com.visualipcv.core.Node;
import com.visualipcv.core.NodeSlot;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorLibrary;
import com.visualipcv.view.GraphView;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GraphController extends Controller<GraphView> {
    private MouseButton selectionButton = MouseButton.PRIMARY;
    private MouseButton dragButton = MouseButton.SECONDARY;

    private double previousMouseX;
    private double previousMouseY;
    private double initialMouseX;
    private double initialMouseY;

    private ObservableList<NodeController> nodes = FXCollections.observableArrayList();
    private ObservableList<ConnectionController> connections = FXCollections.observableArrayList();

    private UIProperty nodesProperty = new UIProperty();
    private UIProperty connectionsProperty = new UIProperty();

    private ConnectionPreviewController connectionPreview;
    private Rectangle selectionPreview;

    public GraphController() {
        super(GraphView.class);
        setContext(new Graph());

        nodes.addListener(new ListChangeListener<NodeController>() {
            @Override
            public void onChanged(Change<? extends NodeController> c) {
                while(c.next()) {
                    if(c.wasAdded()) {
                        for(NodeController node : c.getAddedSubList()) {
                            getView().getInternalPane().getChildren().add(node.getView());
                        }
                    }
                    if(c.wasRemoved()) {
                        for(NodeController node : c.getRemoved()) {
                            getView().getInternalPane().getChildren().remove(node.getView());
                        }
                    }
                }
            }
        });

        connections.addListener(new ListChangeListener<ConnectionController>() {
            @Override
            public void onChanged(Change<? extends ConnectionController> c) {
                while(c.next()) {
                    if(c.wasAdded()) {
                        for(ConnectionController connection : c.getAddedSubList()) {
                            getView().getInternalPane().getChildren().add(connection.getView());
                        }
                    }
                    if(c.wasRemoved()) {
                        for(ConnectionController connection : c.getRemoved()) {
                            getView().getInternalPane().getChildren().remove(connection.getView());
                        }
                    }
                }
            }
        });

        nodesProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                nodes.clear();
                nodes.addAll((List<NodeController>)newValue);
            }
        });

        connectionsProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                connections.clear();
                connections.addAll((List<ConnectionController>)newValue);
            }
        });

        nodesProperty.setBinder((Object graph) -> {
            List<Node> nodes = ((Graph)graph).getNodes();
            List<NodeController> nodeControllers = new ArrayList<>();

            for(Node node : nodes) {
                NodeController nodeController = new NodeController(this);
                nodeController.setContext(node);
                nodeControllers.add(nodeController);
            }

            return nodeControllers;
        });

        connectionsProperty.setBinder((Object graph) -> {
            Set<Connection> connections = ((Graph)graph).getConnections();
            List<ConnectionController> connectionControllers = new ArrayList<>();

            for(Connection connection : connections) {
                ConnectionController connectionController = new ConnectionController(
                        findNodeSlotController(connection.getSource()),
                        findNodeSlotController(connection.getTarget()));
                connectionController.setContext(connection);
                connectionControllers.add(connectionController);
            }

            for(NodeController node : nodes) {
                node.invalidate();
            }

            return connectionControllers;
        });

        initialize();

        getView().addEventFilter(DragEvent.DRAG_OVER, new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if(connectionPreview != null) {
                    Point2D containerPoint = getView().getInternalPane().parentToLocal(new Point2D(event.getX(), event.getY()));
                    connectionPreview.setTarget(containerPoint.getX(), containerPoint.getY());
                    connectionPreview.invalidate();
                }
            }
        });

        getView().addEventFilter(DragEvent.DRAG_DROPPED, new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if(connectionPreview != null) {
                    stopConnectionDrag();
                }
            }
        });

        getView().addEventHandler(MouseEvent.MOUSE_PRESSED, this::onMousePressed);
        getView().addEventHandler(MouseEvent.MOUSE_DRAGGED, this::onMouseDragged);
        getView().addEventHandler(MouseEvent.MOUSE_RELEASED, this::onMouseReleased);

        getView().setOnMouseReleased(this::onMouseReleased);
        getView().setOnDragOver(this::onDragOver);
        getView().setOnDragDropped(this::onDragDropped);
    }

    private NodeSlotController findNodeSlotController(NodeSlot slot) {
        for(NodeController controller : (List<NodeController>)nodesProperty.getValue()) {
            if(controller.getContext() == slot.getNode()) {
                for(AdvancedNodeSlotController slotController : controller.getInputSlots()) {
                    if(slotController.getContext() == slot) {
                        return slotController.getSlot();
                    }
                }
                for(NodeSlotController slotController : controller.getOutputSlots()) {
                    if(slotController.getContext() == slot) {
                        return slotController;
                    }
                }
            }
        }
        return null;
    }

    public List<NodeController> getNodes() {
        return nodes;
    }

    public List<ConnectionController> getConnections() {
        return connections;
    }

    public List<NodeController> getSelectedNodes() {
        List<NodeController> selectedNodes = new ArrayList<>();

        for(NodeController node : nodes) {
            if(node.isSelected()) {
                selectedNodes.add(node);
            }
        }

        return selectedNodes;
    }

    public void startConnectionDrag(NodeSlotController source) {
        connectionPreview = new ConnectionPreviewController(source);
        connectionPreview.setContext(source.getContext());
        getView().getInternalPane().getChildren().add(connectionPreview.getView());
    }

    public void stopConnectionDrag() {
        getView().getInternalPane().getChildren().remove(connectionPreview.getView());
        connectionPreview = null;
    }

    public void onMousePressed(MouseEvent event) {
        previousMouseX = event.getScreenX();
        previousMouseY = event.getScreenY();

        Point2D initial = getView().getInternalPane().parentToLocal(event.getX(), event.getY());
        initialMouseX = initial.getX();
        initialMouseY = initial.getY();

        if(event.getButton() == selectionButton) {
            selectionPreview = new Rectangle();
            selectionPreview.setX(initialMouseX);
            selectionPreview.setY(initialMouseY);
            selectionPreview.setFill(new Color(1.0, 1.0, 1.0, 0.5));
            selectionPreview.setStroke(new Color(0, 0, 0, 1));
            getView().getInternalPane().getChildren().add(selectionPreview);
        }

        if(event.getButton() == selectionButton)
            clearSelection();
    }

    public void onMouseDragged(MouseEvent event) {
        double deltaX = event.getScreenX() - previousMouseX;
        double deltaY = event.getScreenY() - previousMouseY;
        previousMouseX = event.getScreenX();
        previousMouseY = event.getScreenY();

        if(event.getButton() == selectionButton) {
            Point2D newPos = getView().getInternalPane().parentToLocal(event.getX(), event.getY());

            double minX = Math.min(initialMouseX, newPos.getX());
            double maxX = Math.max(initialMouseX, newPos.getX());
            double minY = Math.min(initialMouseY, newPos.getY());
            double maxY = Math.max(initialMouseY, newPos.getY());

            selectionPreview.setWidth(maxX - minX);
            selectionPreview.setHeight(maxY - minY);
            selectionPreview.setX(minX);
            selectionPreview.setY(minY);
        }
    }

    public void onMouseReleased(MouseEvent event) {
        if(event.getButton() == selectionButton && selectionPreview != null) {
            getView().getInternalPane().getChildren().remove(selectionPreview);
            selectionPreview = null;

            Point2D newPos = getView().getInternalPane().parentToLocal(event.getX(), event.getY());
            double minX = Math.min(initialMouseX, newPos.getX());
            double maxX = Math.max(initialMouseX, newPos.getX());
            double minY = Math.min(initialMouseY, newPos.getY());
            double maxY = Math.max(initialMouseY, newPos.getY());

            for(NodeController node : nodes) {
                if(node.getView().getLayoutX() >= minX && node.getView().getLayoutX() + node.getView().getWidth() < maxX &&
                    node.getView().getLayoutY() >= minY && node.getView().getLayoutY() + node.getView().getHeight() < maxY) {
                    node.setSelected(true);
                }
            }
        }
    }

    private Processor getProcessorFromDragEvent(DragEvent event) {
        Dragboard db = event.getDragboard();

        if(!db.hasString())
            return null;

        String content = db.getString();
        String[] params = content.split("/");

        if(params.length != 2)
            return null;

        String module = params[0];
        String name = params[1];

        return ProcessorLibrary.findProcessor(module, name);
    }

    public void onDragOver(DragEvent event) {
        if(getProcessorFromDragEvent(event) != null)
            event.acceptTransferModes(TransferMode.ANY);
    }

    public void onDragDropped(DragEvent event) {
        Processor processor = getProcessorFromDragEvent(event);

        if(processor == null) {
            event.setDropCompleted(false);
            return;
        }

        try {
            double x = event.getX();
            double y = event.getY();
            Point2D p = getView().getInternalPane().getLocalToParentTransform().inverseTransform(x, y);

            ((Graph)getContext()).addNode(new Node(getContext(), processor, p.getX(), p.getY()));
            poll(nodesProperty);

            event.setDropCompleted(true);
        } catch(Exception e) {
            e.printStackTrace();
            event.setDropCompleted(false);
        }
    }

    public void moveSelected(double deltaX, double deltaY) {
        for(NodeController node : getSelectedNodes()) {
            double x = node.getView().getLayoutX();
            double y = node.getView().getLayoutY();
            x += deltaX / getView().getZoom();
            y += deltaY / getView().getZoom();
            node.moveTo(x, y);
        }
    }

    public void removeNode(NodeController node) {
        ((Graph)getContext()).removeNode(node.getContext());
    }

    public void removeSelected() {
        for(NodeController node : getSelectedNodes()) {
            removeNode(node);
        }
    }

    public void clearSelection() {
        for(NodeController node : nodes) {
            node.setSelected(false);
        }
    }

    public void select(NodeController node, boolean ctrlDown) {
        if(!node.isSelected() && !ctrlDown)
            clearSelection();

        node.setSelected(true);
    }

    public void onSave() {
        // TODO: save
    }

    public void onSaveAs() {
        // TODO: save as
    }

    public void onLoad() {
        // TODO: load
    }
}
