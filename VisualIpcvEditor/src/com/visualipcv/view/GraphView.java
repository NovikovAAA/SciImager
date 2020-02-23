package com.visualipcv.view;

import com.visualipcv.controller.IGraphViewElement;
import com.visualipcv.core.Connection;
import com.visualipcv.core.Node;
import com.visualipcv.core.NodeSlot;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorLibrary;
import com.visualipcv.editor.Editor;
import com.visualipcv.viewmodel.GraphViewModel;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableDoubleProperty;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.StyleConverter;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GraphView extends FreePane implements IGraphViewElement {
    private static final CssMetaData<GraphView, Number> CELL_SIZE = new CssMetaData<GraphView, Number>("-fx-grid-cell-size", StyleConverter.getSizeConverter()) {
        @Override
        public boolean isSettable(GraphView styleable) {
            return !styleable.cellSizeProperty.isBound();
        }

        @Override
        public StyleableProperty<Number> getStyleableProperty(GraphView styleable) {
            return styleable.cellSizeProperty;
        }
    };

    private static final CssMetaData<GraphView, Color> BACKGROUND_COLOR = new CssMetaData<GraphView, Color>("-fx-grid-background-color", StyleConverter.getColorConverter()) {
        @Override
        public boolean isSettable(GraphView styleable) {
            return !styleable.backgroundColorProperty.isBound();
        }

        @Override
        public StyleableProperty<Color> getStyleableProperty(GraphView styleable) {
            return styleable.backgroundColorProperty;
        }
    };

    private static final CssMetaData<GraphView, Color> LINE_COLOR = new CssMetaData<GraphView, Color>("-fx-grid-line-color", StyleConverter.getColorConverter()) {
        @Override
        public boolean isSettable(GraphView styleable) {
            return !styleable.lineColorProperty.isBound();
        }

        @Override
        public StyleableProperty<Color> getStyleableProperty(GraphView styleable) {
            return styleable.lineColorProperty;
        }
    };

    private static List<CssMetaData<? extends Styleable, ?>> CSS_META_DATA;

    static {
        final List<CssMetaData<? extends Styleable, ?>> metaData = new ArrayList<>(FreePane.getClassCssMetaData());
        Collections.addAll(metaData, CELL_SIZE, BACKGROUND_COLOR, LINE_COLOR);
        CSS_META_DATA = Collections.unmodifiableList(metaData);
    }

    private GraphViewModel viewModel;

    private MouseButton selectionButton = MouseButton.PRIMARY;
    private MouseButton dragButton = MouseButton.SECONDARY;

    private Canvas canvas;

    private double previousMouseX;
    private double previousMouseY;
    private double initialMouseX;
    private double initialMouseY;

    private ObservableList<NodeView> nodes = FXCollections.observableArrayList();
    private ObservableList<ConnectionView> connections = FXCollections.observableArrayList();

    private ConnectionPreview connectionPreview;
    private Rectangle selectionPreview;

    private StyleableDoubleProperty cellSizeProperty = new SimpleStyleableDoubleProperty(CELL_SIZE, 40.0);
    private StyleableObjectProperty<Color> backgroundColorProperty = new SimpleStyleableObjectProperty<>(BACKGROUND_COLOR, new Color(0.8, 0.8, 0.8, 1.0));
    private StyleableObjectProperty<Color> lineColorProperty = new SimpleStyleableObjectProperty<>(LINE_COLOR, new Color(0.6, 0.6, 0.6, 1.0));

    public GraphView() {
        getStyleClass().setAll("graph");

        viewModel = new GraphViewModel();
        init();
        requestFocus();
    }

    private void init() {
        canvas = new Canvas();
        canvas.setManaged(false);
        canvas.setMouseTransparent(true);
        getChildren().add(canvas);
        getInternalPane().toFront();

        setDragButton(MouseButton.SECONDARY);

        getZoomProperty().bindBidirectional(viewModel.getZoomProperty());
        getXOffsetProperty().bindBidirectional(viewModel.getXOffsetProperty());
        getYOffsetProperty().bindBidirectional(viewModel.getYOffsetProperty());

        setFocusTraversable(true);
        setOnMouseReleased(this::onMouseReleased);
        setOnDragOver(this::onDragOver);
        setOnDragDropped(this::onDragDropped);

        Editor.getPrimaryStage().addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                /*if(event.getCode() == KeyCode.Z && event.isControlDown() && Editor.getDocsPane().getSelectionModel().getSelectedItem().getContent() == GraphView.this)
                    ;
                else if(event.getCode() == KeyCode.Y && event.isControlDown() && Editor.getDocsPane().getSelectionModel().getSelectedItem().getContent() == GraphView.this)
                    ;*/
                if(event.getCode() == KeyCode.Z)
                    zoomToFit();
            }
        });

        addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                GraphView.this.requestFocus();
            }
        });

        addEventFilter(DragEvent.DRAG_OVER, new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if(connectionPreview != null) {
                    Point2D containerPoint = getInternalPane().parentToLocal(new Point2D(event.getX(), event.getY()));
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
                            getInternalPane().getChildren().add(view);
                        }
                    }
                    if(c.wasRemoved()) {
                        for(NodeView view : c.getRemoved()) {
                            viewModel.getNodes().remove(view.getViewModel());
                            getInternalPane().getChildren().remove(view);
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
                            getInternalPane().getChildren().add(view);
                        }
                    }
                    if(c.wasRemoved()) {
                        for(ConnectionView view : c.getRemoved()) {
                            viewModel.getConnections().remove(view.getViewModel());
                            getInternalPane().getChildren().remove(view);
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
        gc.setStroke(lineColorProperty.get());
        gc.setFill(backgroundColorProperty.get());
        gc.fillRect(0.0, 0.0, canvas.getWidth(), canvas.getHeight());

        double cell = cellSizeProperty.get() * getInternalPane().getScaleX();

        double startX = getOffsetX() % cell;
        double startY = getOffsetY() % cell;

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

        for(javafx.scene.Node node : getInternalPane().getChildren()) {
            if(!(node instanceof NodeView))
                continue;

            NodeView nodeView = (NodeView)node;

            if(nodeView.isSelected())
                selectedNodes.add(nodeView);
        }

        return selectedNodes;
    }

    public void updateOrder() {
        FXCollections.sort(getInternalPane().getChildren(), (javafx.scene.Node n1, javafx.scene.Node n2) -> {
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
        getInternalPane().getChildren().add(connectionPreview);
    }

    public void stopConnectionDrag() {
        getInternalPane().getChildren().remove(connectionPreview);
        connectionPreview = null;
    }

    @Override
    public void onMousePressed(MouseEvent event) {
        previousMouseX = event.getScreenX();
        previousMouseY = event.getScreenY();

        Point2D initial = getInternalPane().parentToLocal(event.getX(), event.getY());
        initialMouseX = initial.getX();
        initialMouseY = initial.getY();

        if(event.getButton() == selectionButton) {
            selectionPreview = new Rectangle();
            selectionPreview.setX(initialMouseX);
            selectionPreview.setY(initialMouseY);
            selectionPreview.setFill(new Color(1.0, 1.0, 1.0, 0.5));
            selectionPreview.setStroke(new Color(0, 0, 0, 1));
            getInternalPane().getChildren().add(selectionPreview);
        }

        if(event.getButton() == selectionButton)
            viewModel.clearSelection();

        super.onMousePressed(event);
    }

    @Override
    public void onMouseDragged(MouseEvent event) {
        double deltaX = event.getScreenX() - previousMouseX;
        double deltaY = event.getScreenY() - previousMouseY;
        previousMouseX = event.getScreenX();
        previousMouseY = event.getScreenY();

        if(event.getButton() == selectionButton) {
            Point2D newPos = getInternalPane().parentToLocal(event.getX(), event.getY());

            double minX = Math.min(initialMouseX, newPos.getX());
            double maxX = Math.max(initialMouseX, newPos.getX());
            double minY = Math.min(initialMouseY, newPos.getY());
            double maxY = Math.max(initialMouseY, newPos.getY());

            selectionPreview.setWidth(maxX - minX);
            selectionPreview.setHeight(maxY - minY);
            selectionPreview.setX(minX);
            selectionPreview.setY(minY);
        }

        super.onMouseDragged(event);
    }

    public void onMouseReleased(MouseEvent event) {
        if(event.getButton() == selectionButton && selectionPreview != null) {
            getInternalPane().getChildren().remove(selectionPreview);
            selectionPreview = null;

            Point2D newPos = getInternalPane().parentToLocal(event.getX(), event.getY());
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

    public void onDragOver(DragEvent event) {
        if(getProcessorFromDragEvent(event) != null)
            event.acceptTransferModes(TransferMode.ANY);
    }

    public void onDragDropped(DragEvent event) {
        Processor processor = getProcessorFromDragEvent(event);

        if(processor == null) {
            event.setDropCompleted(false);
        }

        try {
            double x = event.getX();
            double y = event.getY();
            Point2D p = getInternalPane().getLocalToParentTransform().inverseTransform(x, y);
            viewModel.addNode(processor, p.getX(), p.getY());
            event.setDropCompleted(true);
        } catch(Exception e) {
            e.printStackTrace();
            event.setDropCompleted(false);
        }
    }

    public void zoomToFit() {
        if(nodes.isEmpty()) {
            viewModel.setZoom(1.0);
            viewModel.setOffset(0.0, 0.0);
            return;
        }

        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;
        double maxY = -Double.MAX_VALUE;

        for (NodeView node : nodes) {
            minX = Math.min(minX, node.getLayoutX());
            minY = Math.min(minY, node.getLayoutY());
            maxX = Math.max(maxX, node.getLayoutX() + node.getWidth());
            maxY = Math.max(maxY, node.getLayoutY() + node.getHeight());
        }

        double hw = getWidth() * 0.5;
        double hh = getHeight() * 0.5;

        double cx = (maxX + minX) * 0.5;
        double cy = (maxY + minY) * 0.5;
        double rx = getWidth() * 0.5;
        double ry = getHeight() * 0.5;

        double zoomX = getWidth() / (maxX - minX);
        double zoomY = getHeight() / (maxY - minY);

        viewModel.setZoom(Math.min(zoomX, zoomY));
        viewModel.setOffset(rx - cx * getZoom(), ry - cy * getZoom());
    }

    public void onSave() {
        try {
            viewModel.save();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void onSaveAs() {
        try {
            viewModel.saveAs();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onLoad() {
        try {
            viewModel.load();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void updateConnectionViews() {
        for(ConnectionView view : connections)
            view.updatePoints();
    }

    @Override
    public GraphView getGraphView() {
        return this;
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return CSS_META_DATA;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }

    public DoubleProperty cellSizeProperty() {
        return cellSizeProperty;
    }

    public double getCellSize() {
        return cellSizeProperty.get();
    }

    public void setCellSize(double cellSize) {
        this.cellSizeProperty.set(cellSize);
    }

    public ObjectProperty<Color> backgroundColorProperty() {
        return backgroundColorProperty;
    }

    public Color getBackgroundColor() {
        return backgroundColorProperty.get();
    }

    public void setBackgroundColor(Color color) {
        backgroundColorProperty.set(color);
    }

    public ObjectProperty<Color> lineColorProperty() {
        return lineColorProperty;
    }

    public Color getLineColor() {
        return lineColorProperty.get();
    }

    public void setLineColor(Color color) {
        lineColorProperty.set(color);
    }
}
