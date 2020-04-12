package com.visualipcv.controller;

import com.sun.org.apache.bcel.internal.generic.ACONST_NULL;
import com.visualipcv.controller.binding.BindingHelper;
import com.visualipcv.controller.binding.PropertyChangedEventListener;
import com.visualipcv.controller.binding.UIProperty;
import com.visualipcv.core.Connection;
import com.visualipcv.core.Graph;
import com.visualipcv.core.GraphExecutionException;
import com.visualipcv.core.Node;
import com.visualipcv.core.NodeSlot;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorLibrary;
import com.visualipcv.core.io.ConnectionEntity;
import com.visualipcv.core.io.GraphClipboard;
import com.visualipcv.core.io.GraphEntity;
import com.visualipcv.core.io.NodeEntity;
import com.visualipcv.editor.Editor;
import com.visualipcv.view.CustomDataFormats;
import com.visualipcv.view.FunctionListPopup;
import com.visualipcv.view.GraphView;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class GraphController extends Controller<GraphView> {
    private MouseButton selectionButton = MouseButton.PRIMARY;
    private MouseButton dragButton = MouseButton.SECONDARY;

    private double previousMouseX;
    private double previousMouseY;
    private double initialMouseX;
    private double initialMouseY;

    private ObservableList<NodeController> nodes = FXCollections.observableArrayList();
    private ObservableList<ConnectionController> connections = FXCollections.observableArrayList();

    private UIProperty filePathProperty = new UIProperty();
    private UIProperty nodesProperty = new UIProperty();
    private UIProperty connectionsProperty = new UIProperty();

    private ConnectionPreviewController connectionPreview;
    private Rectangle selectionPreview;

    private boolean wasDragged = false;

    public GraphController() {
        super(GraphView.class);
        setContext(new Graph());
        filePathProperty.setValue("New graph");

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
            return BindingHelper.bindList(nodesProperty, ((Graph) graph).getNodes(), (Node node) -> new NodeController(this));
        });

        connectionsProperty.setBinder((Object graph) -> {
            return BindingHelper.bindList(connectionsProperty, ((Graph)graph).getConnections(), (Connection connection) -> new ConnectionController(this));
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

        getView().addEventFilter(DragEvent.DRAG_DONE, new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                stopConnectionDrag();
            }
        });

        getView().addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.DELETE)
                    removeSelected();
                else if(event.getCode() == KeyCode.C && event.isControlDown()) {
                    if(copy()) {
                        event.consume();
                    }
                } else if(event.getCode() == KeyCode.V && event.isControlDown()) {
                    if(past())
                        event.consume();
                } else if(event.getCode() == KeyCode.X && event.isControlDown()) {
                    if(cut())
                        event.consume();
                }
            }
        });

        getView().addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(wasDragged) {
                    event.consume();
                }
            }
        });

        getView().addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                wasDragged = false;
            }
        });

        getView().addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton() == dragButton)
                    wasDragged = true;
            }
        });

        getView().addEventHandler(MouseEvent.MOUSE_PRESSED, this::onMousePressed);
        getView().addEventHandler(MouseEvent.MOUSE_DRAGGED, this::onMouseDragged);
        getView().addEventHandler(MouseEvent.MOUSE_RELEASED, this::onMouseReleased);
        getView().addEventHandler(DragEvent.DRAG_OVER, this::onDragOver);
        getView().addEventHandler(DragEvent.DRAG_DROPPED, this::onDragDropped);

        execution();
    }

    private void execution() {
        /*Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.2), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for(NodeController node : getNodes())
                    node.errorProperty().setValue("");

                try {
                    ((Graph)getContext()).execute();
                } catch (GraphExecutionException e) {

                }

                for(NodeController node : getNodes())
                    node.poll(node.errorProperty());
            }
        }));

        timeline.setCycleCount(-1);
        timeline.play();*/

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    for(NodeController node : getNodes())
                        node.errorProperty().setValue("");

                    try {
                        ((Graph)getContext()).execute();
                    } catch (GraphExecutionException e) {

                    }

                    for(NodeController node : getNodes())
                        node.poll(node.errorProperty());

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {

                    }
                }
            }
        });

        thread.start();
    }

    public NodeSlotController findNodeSlotController(NodeSlot slot) {
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
        if(connectionPreview != null) {
            getView().getInternalPane().getChildren().remove(connectionPreview.getView());
            connectionPreview = null;
        }
    }

    public void onMousePressed(MouseEvent event) {
        previousMouseX = event.getScreenX();
        previousMouseY = event.getScreenY();

        Point2D initial = getView().getInternalPane().parentToLocal(event.getX(), event.getY());
        initialMouseX = initial.getX();
        initialMouseY = initial.getY();

        if(event.getTarget() == getView().getInternalPane() && event.getButton() == selectionButton) {
            selectionPreview = new Rectangle();
            selectionPreview.setX(initialMouseX);
            selectionPreview.setY(initialMouseY);
            selectionPreview.setFill(new Color(1.0, 1.0, 1.0, 0.5));
            selectionPreview.setStroke(new Color(0, 0, 0, 1));
            getView().getInternalPane().getChildren().add(selectionPreview);

            clearSelection();
            event.consume();
        }
    }

    public void onMouseDragged(MouseEvent event) {
        double deltaX = event.getScreenX() - previousMouseX;
        double deltaY = event.getScreenY() - previousMouseY;
        previousMouseX = event.getScreenX();
        previousMouseY = event.getScreenY();

        if(selectionPreview != null && event.getButton() == selectionButton) {
            Point2D newPos = getView().getInternalPane().parentToLocal(event.getX(), event.getY());

            double minX = Math.min(initialMouseX, newPos.getX());
            double maxX = Math.max(initialMouseX, newPos.getX());
            double minY = Math.min(initialMouseY, newPos.getY());
            double maxY = Math.max(initialMouseY, newPos.getY());

            selectionPreview.setWidth(maxX - minX);
            selectionPreview.setHeight(maxY - minY);
            selectionPreview.setX(minX);
            selectionPreview.setY(minY);

            event.consume();
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

        if(event.getButton() == dragButton) {
            Point2D p = getView().getInternalPane().parentToLocal(event.getX(), event.getY());
            FunctionListPopup popup = new FunctionListPopup(this, p.getX(), p.getY(), event.getScreenX(), event.getScreenY());
            popup.show();
        }

        event.consume();
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
        invalidate();
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

    private GraphClipboard createGraphClipboardFromSelection() {
        List<Node> nodes = new ArrayList<>();
        List<Connection> connections = new ArrayList<>();

        for(NodeController node : this.nodes) {
            if(node.isSelected()) {
                nodes.add(node.getContext());
            }
        }

        Set<Node> nodesHash = new HashSet<>(nodes);

        for(Connection connection : ((Graph)getContext()).getConnections()) {
            if(nodesHash.contains(connection.getSource().getNode()) && nodesHash.contains(connection.getTarget().getNode()))
                connections.add(connection);
        }

        return new GraphClipboard(nodes, connections);
    }

    public boolean copy(GraphClipboard graphClipboard) {
        if(graphClipboard.getNodes().isEmpty())
            return false;

        Clipboard clipboard = Clipboard.getSystemClipboard();
        GraphEntity entity = new GraphEntity(graphClipboard);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        for(NodeEntity node : entity.getNodes()) {
            node.addOffset(20.0, 20.0);
        }

        try (ObjectOutputStream stream = new ObjectOutputStream(byteStream)) {
            stream.writeObject(entity);
            stream.flush();

            byte[] data = byteStream.toByteArray();
            ClipboardContent content = new ClipboardContent();
            content.put(CustomDataFormats.OCTET_STREAM, data);
            clipboard.setContent(content);
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public boolean copy() {
        return copy(createGraphClipboardFromSelection());
    }

    public boolean cut() {
        GraphClipboard clipboard = createGraphClipboardFromSelection();
        boolean res = copy(clipboard);

        for(NodeController node : getSelectedNodes())
            removeNode(node);

        return res;
    }

    public boolean past() {
        ByteArrayInputStream byteStream;
        byte[] bytes;

        try {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            bytes = (byte[])clipboard.getContent(CustomDataFormats.OCTET_STREAM);
        } catch(Exception e) {
            return false;
        }

        byteStream = new ByteArrayInputStream(bytes);

        try (ObjectInputStream stream = new ObjectInputStream(byteStream)) {
            GraphEntity entity = (GraphEntity)stream.readObject();
            Graph graph = getContext();
            Map<UUID, UUID> uidMapping = new HashMap<>();

            clearSelection();
            Set<Node> nodesToSelect = new HashSet<>();

            for(NodeEntity node : entity.getNodes()) {
                UUID oldId = node.getId();
                node.resetUID();
                UUID newId = node.getId();

                Node newNode = new Node(graph, node);
                graph.addNode(newNode);
                uidMapping.put(oldId, newId);
                nodesToSelect.add(newNode);
            }

            for(ConnectionEntity connection : entity.getConnections()) {
                UUID newSourceId = uidMapping.get(connection.getSourceNodeId());
                UUID newTargetId = uidMapping.get(connection.getTargetNodeId());
                connection.updateUIDs(newSourceId, newTargetId);
                graph.addConnection(new Connection(graph, connection));
            }

            stream.close();
            invalidate();

            for(NodeController controller : nodes) {
                if(nodesToSelect.contains(controller.<Node>getContext())) {
                    controller.setSelected(true);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            return false;
        }

        return true;
    }

    public void select(NodeController node, boolean ctrlDown) {
        if(!node.isSelected() && !ctrlDown)
            clearSelection();

        node.setSelected(true);
    }

    public void save() {
        File file = new File((String)filePathProperty.getValue());

        if(!file.exists()) {
            saveAs();
        } else {
            try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file))) {
                stream.writeObject(new GraphEntity((Graph)getContext()));
                filePathProperty.setValue(file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void load() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open");

        File file = chooser.showOpenDialog(Editor.getPrimaryStage().getScene().getWindow());

        if(file != null) {
            try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream(file))) {
                setContext(new Graph((GraphEntity)stream.readObject()));
                filePathProperty.setValue(file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveAs() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save");

        File file = chooser.showSaveDialog(Editor.getPrimaryStage().getScene().getWindow());

        if(file != null) {
            try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file))) {
                stream.writeObject(new GraphEntity((Graph)getContext()));
                filePathProperty.setValue(file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public UIProperty filePathProperty() {
        return filePathProperty;
    }
}
