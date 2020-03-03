package com.visualipcv.view;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class FreePane extends AnchorPane {
    private Pane internalPane;

    private double previousMouseX;
    private double previousMouseY;

    private MouseButton dragButton = MouseButton.SECONDARY;
    private DoubleProperty xOffset = new SimpleDoubleProperty(0.0);
    private DoubleProperty yOffset = new SimpleDoubleProperty(0.0);
    private DoubleProperty zoom = new SimpleDoubleProperty(1.0);

    public FreePane() {
        getStyleClass().add("free-pane");

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

        setOnMousePressed(this::onMousePressed);
        setOnMouseDragged(this::onMouseDragged);
        setOnScroll(this::onScroll);

        getChildren().add(internalPane);
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

    public void onMousePressed(MouseEvent event) {
        previousMouseX = event.getScreenX();
        previousMouseY = event.getScreenY();
        event.consume();
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

        value = Math.min(20.0, Math.max(value, 0.1));
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
    }
}
