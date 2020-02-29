package com.visualipcv.controller;

import com.visualipcv.controller.ConnectionBaseController;
import com.visualipcv.controller.NodeSlotController;
import com.visualipcv.core.NodeSlot;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class ConnectionPreviewController extends ConnectionBaseController {
    private NodeSlotController source;

    public ConnectionPreviewController(NodeSlotController source) {
        super();
        this.source = source;

        sourceXProperty.setBinder((Object slot) -> {
            return localToContainerCoords(source.getView(), source.getView().getWidth() * 0.5, source.getView().getHeight() * 0.5).getX();
        });

        sourceYProperty.setBinder((Object slot) -> {
            return localToContainerCoords(source.getView(), source.getView().getWidth() * 0.5, source.getView().getHeight() * 0.5).getY();
        });

        paintProperty.setBinder((Object slot) -> {
            java.awt.Color color = ((NodeSlot)slot).getProperty().getType().getColor();
            return new Color(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0, 1.0);
        });

        initialize();
    }
}
