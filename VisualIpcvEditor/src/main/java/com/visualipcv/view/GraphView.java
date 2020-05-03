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
    public GraphView() {
        super();
        getStyleClass().setAll("graph");
        requestFocus();
        init();
    }

    private void init() {
        setPrefWidth(1280.0);
        setPrefHeight(720.0);

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
}
