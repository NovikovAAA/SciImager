package com.visualipcv.view;

import com.visualipcv.core.Node;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorLibrary;
import com.visualipcv.viewmodel.GraphViewModel;
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
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.io.IOException;

public class GraphView extends AnchorPane {
    private GraphViewModel viewModel = new GraphViewModel();

    @FXML
    private Pane container;
    @FXML
    private Canvas canvas;

    private double previousMouseX;
    private double previousMouseY;

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

        viewModel.getNodeList().addListener(new ListChangeListener<Node>() {
            @Override
            public void onChanged(Change<? extends Node> c) {
                if(c.next()) {
                    if(c.wasAdded()) {
                        for(Node node : c.getAddedSubList()) {
                            container.getChildren().add(new NodeView(node));
                        }
                    }
                    /*if(c.wasRemoved()) {
                        for(Node node : c.getRemoved()) {
                            nodes.remove();
                        }
                    }*/
                }
            }
        });
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

    @FXML
    public void onMousePressed(MouseEvent event) {
        previousMouseX = event.getScreenX();
        previousMouseY = event.getScreenY();
    }

    @FXML
    public void onMouseDragged(MouseEvent event) {
        double deltaX = event.getScreenX() - previousMouseX;
        double deltaY = event.getScreenY() - previousMouseY;
        xOffset.setValue(xOffset.getValue() + deltaX);
        yOffset.setValue(yOffset.getValue() + deltaY);
        previousMouseX = event.getScreenX();
        previousMouseY = event.getScreenY();
    }

    @FXML
    public void onDragOver(DragEvent event) {
        event.acceptTransferModes(TransferMode.ANY);
    }

    @FXML
    public void onDragDropped(DragEvent event) {
        Dragboard db = event.getDragboard();

        if(!db.hasString())
            return;

        String content = db.getString();
        String module = content.split("/")[0];
        String name = content.split("/")[1];

        Processor processor = ProcessorLibrary.findProcessor(module, name);

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
    }
}
