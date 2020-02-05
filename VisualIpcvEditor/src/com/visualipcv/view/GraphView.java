package com.visualipcv.view;

import com.sun.corba.se.pept.transport.ConnectionCache;
import com.visualipcv.controller.GraphViewController;
import com.visualipcv.controller.IGraphViewElement;
import com.visualipcv.core.Connection;
import com.visualipcv.core.Node;
import com.visualipcv.core.NodeSlot;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorLibrary;
import com.visualipcv.core.command.ConnectCommand;
import com.visualipcv.viewmodel.GraphViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GraphView extends AnchorPane implements IGraphViewElement {
    private GraphViewController controller = new GraphViewController(this);
    private GraphViewModel viewModel = new GraphViewModel(controller);

    @FXML
    private Pane container;
    @FXML
    private Canvas canvas;

    private double previousMouseX;
    private double previousMouseY;

    private ObservableList<NodeView> nodes = FXCollections.observableArrayList();
    private ObservableList<ConnectionView> connections = FXCollections.observableArrayList();

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
    }

    private NodeView findNodeViewByModel(Node node) {
        for(javafx.scene.Node n : container.getChildren()) {
            if(n instanceof NodeView) {
                if(((NodeView) n).getViewModel().getNode() == node) {
                    return (NodeView)n;
                }
            }
        }
        return null;
    }

    private NodeSlotView findSlotViewByModel(NodeSlot nodeSlot) {
        NodeView view = findNodeViewByModel(nodeSlot.getNode());

        if(view == null)
            return null;

        return view.findSlotViewByModel(nodeSlot);
    }

    public List<NodeView> getNodes() {
        return nodes;
    }

    public List<ConnectionView> getConnections() {
        return connections;
    }

    @Override
    public void layoutChildren() {
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

    public void clearSelection() {
        for(javafx.scene.Node node : container.getChildren()) {
            if(node instanceof NodeView) {
                NodeView nodeView = (NodeView)node;
                nodeView.setSelected(false);
            }
        }
    }

    public void moveSelectedNodes(double deltaX, double deltaY) {
        for(NodeView node : getSelectedNodes()) {
            node.setLayoutX(node.getLayoutX() + deltaX / container.getScaleX());
            node.setLayoutY(node.getLayoutY() + deltaY / container.getScaleY());
        }
    }

    public void removeSelectedNodes() {
        /*for(NodeView nodeView : getSelectedNodes()) {
            viewModel.removeNode(nodeView.getViewModel().getNode());
        }*/
    }

    public void selectNode(NodeView nodeView, boolean ctrlPressed) {
        if(!ctrlPressed && !nodeView.isSelected())
            clearSelection();

        nodeView.setSelected(true);
        updateOrder();
    }

    public void updateOrder() {
        getChildren().sort((javafx.scene.Node n1, javafx.scene.Node n2) -> {
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

    @FXML
    public void onMousePressed(MouseEvent event) throws NonInvertibleTransformException {
        previousMouseX = event.getScreenX();
        previousMouseY = event.getScreenY();
        clearSelection();
        event.consume();
    }

    @FXML
    public void onMouseDragged(MouseEvent event) {
        double deltaX = event.getScreenX() - previousMouseX;
        double deltaY = event.getScreenY() - previousMouseY;
        previousMouseX = event.getScreenX();
        previousMouseY = event.getScreenY();

        if(event.getTarget() == this || event.getTarget() == container) {
            xOffset.setValue(xOffset.getValue() + deltaX / container.getScaleX());
            yOffset.setValue(yOffset.getValue() + deltaY / container.getScaleY());
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
        double x = container.getScaleX();
        double y = container.getScaleY();

        x += event.getDeltaY() * 0.01;
        y += event.getDeltaY() * 0.01;
        x = Math.min(10.0, Math.max(0.2, x));
        y = Math.min(10.0, Math.max(0.2, y));

        container.setScaleX(x);
        container.setScaleY(y);
        repaintGrid();

        event.consume();
    }

    @Override
    public GraphViewController getController() {
        return controller;
    }

    @Override
    public GraphView getGraphView() {
        return this;
    }
}
