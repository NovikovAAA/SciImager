package com.visualipcv.view;

import com.visualipcv.controller.IGraphViewElement;
import com.visualipcv.core.Connection;
import com.visualipcv.core.Node;
import com.visualipcv.core.NodeSlot;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorLibrary;
import com.visualipcv.viewmodel.GraphViewModel;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.NonInvertibleTransformException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GraphView extends AnchorPane implements IGraphViewElement {
    private GraphViewModel viewModel = new GraphViewModel();

    private MouseButton selectionButton = MouseButton.PRIMARY;
    private MouseButton dragButton = MouseButton.SECONDARY;

    @FXML
    private Pane container;
    @FXML
    private Canvas canvas;

    private double previousMouseX;
    private double previousMouseY;
    private double initialMouseX;
    private double initialMouseY;

    private ObservableList<NodeView> nodes = FXCollections.observableArrayList();
    private ObservableList<ConnectionView> connections = FXCollections.observableArrayList();

    private ConnectionPreview connectionPreview;
    private Rectangle selectionPreview;

    private DoubleProperty cellSize = new DoublePropertyBase(40.0) {
        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "cellSize";
        }

        @Override
        public void invalidated() {
            repaintGrid();
        }
    };

    private DoubleProperty xOffset = new DoublePropertyBase() {
        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "xOffset";
        }

        @Override
        public void invalidated() {
            requestLayout();
        }
    };

    private DoubleProperty yOffset = new DoublePropertyBase() {
        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "yOffset";
        }

        @Override
        public void invalidated() {
            requestLayout();
        }
    };

    private DoubleProperty zoom = new DoublePropertyBase(1.0) {
        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "zoom";
        }

        @Override
        public void invalidated() {
            requestLayout();
        }
    };

    public GraphView() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("GraphView.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch(IOException e) {
            e.printStackTrace();
        }

        container.layoutXProperty().bind(xOffset);
        container.layoutYProperty().bind(yOffset);
        zoom.bind(viewModel.getZoomProperty());
        container.scaleXProperty().bind(zoom);
        container.scaleYProperty().bind(zoom);

        addEventFilter(DragEvent.DRAG_OVER, new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if(connectionPreview != null) {
                    Point2D containerPoint = container.parentToLocal(new Point2D(event.getX(), event.getY()));
                    connectionPreview.setTarget(containerPoint.getX(), containerPoint.getY());
                }
            }
        });

        addEventFilter(DragEvent.DRAG_DONE, new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if(connectionPreview != null) {
                    stopConnectionDrag();
                }
            }
        });

        nodes.addListener(new ListChangeListener<NodeView>() {
            @Override
            public void onChanged(Change<? extends NodeView> c) {
                while(c.next()) {
                    if(c.wasAdded()) {
                        for (NodeView view : c.getAddedSubList()) {
                            viewModel.getNodes().add(view.getViewModel());
                            container.getChildren().add(view);
                        }
                    }
                    if(c.wasRemoved()) {
                        for(NodeView view : c.getRemoved()) {
                            viewModel.getNodes().remove(view.getViewModel());
                            container.getChildren().remove(view);
                        }
                    }
                }
            }
        });

        connections.addListener(new ListChangeListener<ConnectionView>() {
            @Override
            public void onChanged(Change<? extends ConnectionView> c) {
                while(c.next()) {
                    if(c.wasAdded()) {
                        for(ConnectionView view : c.getAddedSubList()) {
                            viewModel.getConnections().add(view.getViewModel());
                            container.getChildren().add(view);
                        }
                    }
                    if(c.wasRemoved()) {
                        for(ConnectionView view : c.getRemoved()) {
                            viewModel.getConnections().remove(view.getViewModel());
                            container.getChildren().remove(view);
                        }
                    }
                }
            }
        });

        viewModel.addListener(new GraphViewModel.IGraphEventListener() {
            @Override
            public void onAdded(Node node) {
                NodeView view = new NodeView(GraphView.this, node);
                nodes.add(view);
            }

            @Override
            public void onRemoved(Node node) {
                for(NodeView view : nodes) {
                    if(view.getViewModel().getNode() == node) {
                        nodes.remove(view);
                        break;
                    }
                }
            }

            @Override
            public void onConnected(Connection connection) {
                connections.add(new ConnectionView(findNodeSlotView(connection.getSource()), findNodeSlotView(connection.getTarget())));
            }

            @Override
            public void onDisconnected(Connection connection) {
                for(ConnectionView view : connections) {
                    if(view.getViewModel().getSource().getNodeSlot() == connection.getSource() &&
                        view.getViewModel().getTarget().getNodeSlot() == connection.getTarget()) {
                        connections.remove(view);
                        break;
                    }
                }
            }

            @Override
            public void onRequestSort() {
                updateOrder();
            }
        });
    }

    public NodeSlotView findNodeSlotView(NodeSlot slot) {
        for(NodeView node : nodes) {
            for(AdvancedNodeSlotView slotView : node.getInputSlots()) {
                if(slotView.getSlotView() == null)
                    continue;
                if(slotView.getSlotView().getViewModel().getNodeSlot() == slot) {
                    return slotView.getSlotView();
                }
            }
            for(NodeSlotView slotView : node.getOutputSlots()) {
                if(slotView.getViewModel().getNodeSlot() == slot) {
                    return slotView;
                }
            }
        }
        return null;
    }

    public List<NodeView> getNodes() {
        return nodes;
    }

    public List<ConnectionView> getConnections() {
        return connections;
    }

    @Override
    public void layoutChildren() {
        super.layoutChildren();
        canvas.setWidth(getWidth());
        canvas.setHeight(getHeight());
        repaintGrid();
    }

    public void repaintGrid() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setLineWidth(1.0f);
        gc.setStroke(new Color(0.6, 0.6, 0.6, 1.0));
        gc.setFill(new Color(0.8, 0.8, 0.8, 1.0));
        gc.fillRect(0.0, 0.0, canvas.getWidth(), canvas.getHeight());

        double cell = cellSize.get() * container.getScaleX();

        double startX = xOffset.getValue() % cell;
        double startY = yOffset.getValue() % cell;

        while(startX < getWidth()) {
            gc.strokeLine(startX, 0.0, startX, getHeight());
            startX += cell;
        }

        while(startY < getHeight()) {
            gc.strokeLine(0.0, startY, getWidth(), startY);
            startY += cell;
        }

        gc.stroke();
    }

    public GraphViewModel getViewModel() {
        return viewModel;
    }

    public List<NodeView> getSelectedNodes() {
        List<NodeView> selectedNodes = new ArrayList<>();

        for(javafx.scene.Node node : container.getChildren()) {
            if(!(node instanceof NodeView))
                continue;

            NodeView nodeView = (NodeView)node;

            if(nodeView.isSelected())
                selectedNodes.add(nodeView);
        }

        return selectedNodes;
    }

    public void updateOrder() {
        FXCollections.sort(container.getChildren(), (javafx.scene.Node n1, javafx.scene.Node n2) -> {
            if(n1.getClass() == n2.getClass()) {
                if(n1 instanceof NodeView) {
                    if(((NodeView) n1).isSelected())
                        return 1;
                    else if(((NodeView) n2).isSelected())
                        return -1;
                    return 0;
                }
            } else if(n1 instanceof ConnectionView) {
                return 1;
            } else if(n2 instanceof ConnectionView) {
                return -1;
            }
            return 0;
        });
    }

    public void startConnectionDrag(NodeSlotView source) {
        connectionPreview = new ConnectionPreview(source);
        container.getChildren().add(connectionPreview);
    }

    public void stopConnectionDrag() {
        container.getChildren().remove(connectionPreview);
        connectionPreview = null;
    }

    @FXML
    public void onMousePressed(MouseEvent event) throws NonInvertibleTransformException {
        previousMouseX = event.getScreenX();
        previousMouseY = event.getScreenY();

        Point2D initial = container.parentToLocal(event.getX(), event.getY());
        initialMouseX = initial.getX();
        initialMouseY = initial.getY();

        if(event.getButton() == selectionButton) {
            selectionPreview = new Rectangle();
            selectionPreview.setX(initialMouseX);
            selectionPreview.setY(initialMouseY);
            selectionPreview.setFill(new Color(1.0, 1.0, 1.0, 0.5));
            selectionPreview.setStroke(new Color(0, 0, 0, 1));
            container.getChildren().add(selectionPreview);
        }

        if(event.getButton() == selectionButton)
            viewModel.clearSelection();

        event.consume();
    }

    @FXML
    public void onMouseDragged(MouseEvent event) {
        double deltaX = event.getScreenX() - previousMouseX;
        double deltaY = event.getScreenY() - previousMouseY;
        previousMouseX = event.getScreenX();
        previousMouseY = event.getScreenY();

        if(event.getButton() == dragButton && (event.getTarget() == this || event.getTarget() == container)) {
            xOffset.setValue(xOffset.getValue() + deltaX);
            yOffset.setValue(yOffset.getValue() + deltaY);
        }

        if(event.getButton() == selectionButton) {
            Point2D newPos = container.parentToLocal(event.getX(), event.getY());

            double minX = Math.min(initialMouseX, newPos.getX());
            double maxX = Math.max(initialMouseX, newPos.getX());
            double minY = Math.min(initialMouseY, newPos.getY());
            double maxY = Math.max(initialMouseY, newPos.getY());

            selectionPreview.setWidth(maxX - minX);
            selectionPreview.setHeight(maxY - minY);
            selectionPreview.setX(minX);
            selectionPreview.setY(minY);
        }

        event.consume();
    }

    @FXML
    public void onMouseReleased(MouseEvent event) {
        if(event.getButton() == selectionButton && selectionPreview != null) {
            container.getChildren().remove(selectionPreview);
            selectionPreview = null;

            Point2D newPos = container.parentToLocal(event.getX(), event.getY());
            double minX = Math.min(initialMouseX, newPos.getX());
            double maxX = Math.max(initialMouseX, newPos.getX());
            double minY = Math.min(initialMouseY, newPos.getY());
            double maxY = Math.max(initialMouseY, newPos.getY());

            for(NodeView view : nodes) {
                if(view.getLayoutX() >= minX && view.getLayoutX() + view.getWidth() < maxX &&
                    view.getLayoutY() >= minY && view.getLayoutY() + view.getHeight() < maxY) {
                    viewModel.selectNode(view.getViewModel());
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

    @FXML
    public void onDragOver(DragEvent event) {
        if(getProcessorFromDragEvent(event) != null)
            event.acceptTransferModes(TransferMode.ANY);
    }

    @FXML
    public void onDragDropped(DragEvent event) {
        Processor processor = getProcessorFromDragEvent(event);

        if(processor == null) {
            event.setDropCompleted(false);
        }

        try {
            double x = event.getX();
            double y = event.getY();
            Point2D p = container.getLocalToParentTransform().inverseTransform(x, y);
            viewModel.addNode(processor, p.getX(), p.getY());
            event.setDropCompleted(true);
        } catch(Exception e) {
            e.printStackTrace();
            event.setDropCompleted(false);
        }
    }

    @FXML
    public void onScroll(ScrollEvent event) {
        viewModel.zoom(event.getDeltaY() * 0.003);
        repaintGrid();
        event.consume();
    }

    @Override
    public GraphView getGraphView() {
        return this;
    }

    public double getOffsetX() {
        return xOffset.get();
    }

    public void setOffsetX(double xOffset) {
        this.xOffset.set(xOffset);
    }

    public double getOffsetY() {
        return yOffset.get();
    }

    public void setOffsetY(double yOffset) {
        this.yOffset.set(yOffset);
    }

    public double getCellSize() {
        return cellSize.get();
    }

    public void setCellSize(double cellSize) {
        this.cellSize.set(cellSize);
    }

    public double getZoom() {
        return zoom.get();
    }

    public DoubleProperty getXOffsetProperty() {
        return xOffset;
    }

    public DoubleProperty getYOffsetProperty() {
        return yOffset;
    }

    public DoubleProperty getCellSizeProperty() {
        return cellSize;
    }

    public DoubleProperty getZoomProperty() {
        return zoom;
    }
}
