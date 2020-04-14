package com.visualipcv.view;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableDoubleProperty;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.StyleConverter;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GraphView extends FreePane {
    private static List<CssMetaData<? extends Styleable, ?>> CSS_META_DATA;

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

    static {
        final List<CssMetaData<? extends Styleable, ?>> metaData = new ArrayList<>(FreePane.getClassCssMetaData());
        Collections.addAll(metaData, CELL_SIZE, BACKGROUND_COLOR, LINE_COLOR);
        CSS_META_DATA = Collections.unmodifiableList(metaData);
    }

    private StyleableDoubleProperty cellSizeProperty = new SimpleStyleableDoubleProperty(CELL_SIZE, 40.0);
    private StyleableObjectProperty<Color> backgroundColorProperty = new SimpleStyleableObjectProperty<>(BACKGROUND_COLOR, new Color(0.8, 0.8, 0.8, 1.0));
    private StyleableObjectProperty<Color> lineColorProperty = new SimpleStyleableObjectProperty<>(LINE_COLOR, new Color(0.6, 0.6, 0.6, 1.0));

    private Canvas canvas;

    public GraphView() {
        super();
        getStyleClass().setAll("graph");
        requestFocus();
        init();
    }

    private void init() {
        setPrefWidth(1280.0);
        setPrefHeight(720.0);

        canvas = new Canvas();
        canvas.setManaged(false);
        canvas.setMouseTransparent(true);
        getChildren().add(canvas);
        getInternalPane().toFront();

        setDragButton(MouseButton.SECONDARY);
        setFocusTraversable(true);

        addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.Z) {
                    zoomToFit();
                }
            }
        });

        addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                requestFocus();
            }
        });
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

    @Override
    public void layoutChildren() {
        super.layoutChildren();
        canvas.setWidth(getWidth());
        canvas.setHeight(getHeight());
        repaintGrid();
    }

    public void updateOrder() {
        /*FXCollections.sort(getInternalPane().getChildren(), (javafx.scene.Node n1, javafx.scene.Node n2) -> {
            if(n1.getClass() == n2.getClass()) {
                if(n1 instanceof NodeController) {
                    if(((NodeController) n1).isSelected())
                        return 1;
                    else if(((NodeController) n2).isSelected())
                        return -1;
                    return 0;
                }
            } else if(n1 instanceof ConnectionController) {
                return 1;
            } else if(n2 instanceof ConnectionController) {
                return -1;
            }
            return 0;
        });*/
    }

    public void zoomToFit() {
        /*if(getInternalPane().getChildren().isEmpty()) {
            viewModel.setZoom(1.0);
            viewModel.setOffset(0.0, 0.0);
            return;
        }

        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;
        double maxY = -Double.MAX_VALUE;

        for (javafx.scene.Node node : getInternalPane().getChildren()) {
            minX = Math.min(minX, node.getLayoutX());
            minY = Math.min(minY, node.getLayoutY());

            if(node instanceof Region) {
                Region region = (Region)node;
                maxX = Math.max(maxX, node.getLayoutX() + region.getWidth());
                maxY = Math.max(maxY, node.getLayoutY() + region.getHeight());
            }
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
        viewModel.setOffset(rx - cx * getZoom(), ry - cy * getZoom());*/
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