package com.visualipcv.view;

import javafx.geometry.Point2D;
import javafx.scene.shape.CubicCurve;

public class ConnectionPreview extends ConnectionViewBase {
    private NodeSlotView source;

    public ConnectionPreview(NodeSlotView source) {
        this.source = source;

        getPaintProperty().bind(source.getViewModel().getStrokeProperty());
        Point2D point = localToContainerCoords(source, source.getWidth() * 0.5, source.getHeight() * 0.5);
        setSource(point.getX(), point.getY());
    }
}
