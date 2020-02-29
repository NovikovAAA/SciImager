package com.visualipcv.controller;

import com.visualipcv.core.Connection;
import javafx.scene.paint.Color;

public class ConnectionController extends ConnectionBaseController {
    private Connection connection;
    private NodeSlotController source;
    private NodeSlotController target;

    public ConnectionController(NodeSlotController source, NodeSlotController target) {
        super();
        this.source = source;
        this.target = target;

        sourceXProperty.setBinder((Object connection) -> {
            return localToContainerCoords(source.getView(), source.getView().getWidth() * 0.5, source.getView().getHeight() * 0.5).getX();
        });

        sourceYProperty.setBinder((Object connection) -> {
            return localToContainerCoords(source.getView(), source.getView().getWidth() * 0.5, source.getView().getHeight() * 0.5).getY();
        });

        targetXProperty.setBinder((Object connection) -> {
            return localToContainerCoords(target.getView(), target.getView().getWidth() * 0.5, target.getView().getHeight() * 0.5).getX();
        });

        targetYProperty.setBinder((Object connection) -> {
            return localToContainerCoords(target.getView(), target.getView().getWidth() * 0.5, target.getView().getHeight() * 0.5).getY();
        });

        paintProperty.setBinder((Object connection) -> {
            java.awt.Color color = ((Connection)connection).getSource().getProperty().getType().getColor();
            return new Color(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0, 1.0);
        });
    }

    public NodeSlotController getSourceSlot() {
        return source;
    }

    public NodeSlotController getTargetSlot() {
        return target;
    }
}
