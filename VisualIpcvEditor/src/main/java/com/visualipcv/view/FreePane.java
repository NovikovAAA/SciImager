package com.visualipcv.view;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FreePane extends AnchorPane {
    private static List<CssMetaData<? extends Styleable, ?>> CSS_META_DATA;

    private static final CssMetaData<FreePane, Number> CELL_SIZE = new CssMetaData<FreePane, Number>("-fx-grid-cell-size", StyleConverter.getSizeConverter()) {
        @Override
        public boolean isSettable(FreePane styleable) {
            return !styleable.cellSizeProperty.isBound();
        }

        @Override
        public StyleableProperty<Number> getStyleableProperty(FreePane styleable) {
            return styleable.cellSizeProperty;
        }
    };

    private static final CssMetaData<FreePane, Color> BACKGROUND_COLOR = new CssMetaData<FreePane, Color>("-fx-grid-background-color", StyleConverter.getColorConverter()) {
        @Override
        public boolean isSettable(FreePane styleable) {
            return !styleable.backgroundColorProperty.isBound();
        }

        @Override
        public StyleableProperty<Color> getStyleableProperty(FreePane styleable) {
            return styleable.backgroundColorProperty;
        }
    };

    private static final CssMetaData<FreePane, Color> LINE_COLOR = new CssMetaData<FreePane, Color>("-fx-grid-line-color", StyleConverter.getColorConverter()) {
        @Override
        public boolean isSettable(FreePane styleable) {
            return !styleable.lineColorProperty.isBound();
        }

        @Override
        public StyleableProperty<Color> getStyleableProperty(FreePane styleable) {
            return styleable.lineColorProperty;
        }
    };

    static {
        final List<CssMetaData<? extends Styleable, ?>> metaData = new ArrayList<>(AnchorPane.getClassCssMetaData());
        Collections.addAll(metaData, CELL_SIZE, BACKGROUND_COLOR, LINE_COLOR);
        CSS_META_DATA = Collections.unmodifiableList(metaData);
    }

    private Canvas canvas;

    private StyleableDoubleProperty cellSizeProperty = new SimpleStyleableDoubleProperty(CELL_SIZE, 20.0);
    private StyleableObjectProperty<Color> backgroundColorProperty = new SimpleStyleableObjectProperty<>(BACKGROUND_COLOR, new Color(0.8, 0.8, 0.8, 1.0));
    private StyleableObjectProperty<Color> lineColorProperty = new SimpleStyleableObjectProperty<>(LINE_COLOR, new Color(0.7, 0.7, 0.7, 1.0));

    private Pane internalPane;

    private double previousMouseX;
    private double previousMouseY;

    private MouseButton dragButton = MouseButton.SECONDARY;
    private DoubleProperty xOffset = new SimpleDoubleProperty(0.0);
    private DoubleProperty yOffset = new SimpleDoubleProperty(0.0);
    private DoubleProperty zoom = new SimpleDoubleProperty(1.0);

    public FreePane() {
        getStyleClass().add("free-pane");

        canvas = new Canvas();
        canvas.setManaged(false);
        canvas.setMouseTransparent(true);

        internalPane = new Pane();
        internalPane.setPrefWidth(0.0);
        internalPane.setPrefHeight(0.0);
        internalPane.setManaged(false);

        internalPane.layoutXProperty().bind(xOffset);
        internalPane.layoutYProperty().bind(yOffset);
        internalPane.scaleXProperty().bind(zoom);
        internalPane.scaleYProperty().bind(zoom);

        xOffset.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                requestLayout();
            }
        });

        yOffset.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                requestLayout();
            }
        });

        zoom.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                requestLayout();
            }
        });

        addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                previousMouseX = event.getScreenX();
                previousMouseY = event.getScreenY();
            }
        });

        addEventHandler(MouseEvent.MOUSE_DRAGGED, this::onMouseDragged);
        addEventHandler(ScrollEvent.SCROLL, this::onScroll);

        getChildren().add(internalPane);
        getChildren().add(canvas);
        getInternalPane().toFront();
    }

    public void setDragButton(MouseButton button) {
        dragButton = button;
    }

    public MouseButton getDragButton() {
        return dragButton;
    }

    public DoubleProperty getXOffsetProperty() {
        return xOffset;
    }

    public DoubleProperty getYOffsetProperty() {
        return yOffset;
    }

    public DoubleProperty getZoomProperty() {
        return zoom;
    }

    public Pane getInternalPane() {
        return internalPane;
    }

    public double getOffsetX() {
        return xOffset.get();
    }

    public double getOffsetY() {
        return yOffset.get();
    }

    public double getZoom() {
        return zoom.get();
    }

    public void setOffsetX(double x) {
        xOffset.set(x);
    }

    public void setOffsetY(double y) {
        yOffset.set(y);
    }

    public void setZoom(double zoom) {
        this.zoom.set(zoom);
    }

    public void onMouseDragged(MouseEvent event) {
        double deltaX = event.getScreenX() - previousMouseX;
        double deltaY = event.getScreenY() - previousMouseY;
        previousMouseX = event.getScreenX();
        previousMouseY = event.getScreenY();

        if(event.getButton() == dragButton) {
            xOffset.set(xOffset.get() + deltaX);
            yOffset.set(yOffset.get() + deltaY);
        }

        event.consume();
    }

    public void onScroll(ScrollEvent event) {
        Point2D point = internalPane.parentToLocal(event.getX(), event.getY());

        double delta = event.getDeltaY() * 0.003;
        double value = zoom.get() + delta;

        value = Math.min(2.0, Math.max(value, 0.25));
        double deltaScale = value - zoom.get();
        double deltaX = -event.getX() * deltaScale;
        double deltaY = -event.getY() * deltaScale;

        zoom.set(value);
        xOffset.set(xOffset.get() + deltaX);
        yOffset.set(yOffset.get() + deltaY);

        event.consume();
    }

    @Override
    public void layoutChildren() {
        super.layoutChildren();
        setClip(new Rectangle(0.0, 0.0, getWidth(), getHeight()));
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
