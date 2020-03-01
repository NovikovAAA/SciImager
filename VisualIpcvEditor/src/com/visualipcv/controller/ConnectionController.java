package com.visualipcv.controller;

import com.visualipcv.core.Connection;
import javafx.scene.paint.Color;

public class ConnectionController extends ConnectionBaseController {
    private GraphController graphController;

    public ConnectionController(GraphController controller) {
        super();
        this.graphController = controller;

        sourceXProperty.setBinder((Object connection) -> {
            return localToContainerCoords(getSourceSlot().getView(), getSourceSlot().getView().getWidth() * 0.5, getSourceSlot().getView().getHeight() * 0.5).getX();
        });

        sourceYProperty.setBinder((Object connection) -> {
            return localToContainerCoords(getSourceSlot().getView(), getSourceSlot().getView().getWidth() * 0.5, getSourceSlot().getView().getHeight() * 0.5).getY();
        });

        targetXProperty.setBinder((Object connection) -> {
            return localToContainerCoords(getTargetSlot().getView(), getTargetSlot().getView().getWidth() * 0.5, getTargetSlot().getView().getHeight() * 0.5).getX();
        });

        targetYProperty.setBinder((Object connection) -> {
            return localToContainerCoords(getTargetSlot().getView(), getTargetSlot().getView().getWidth() * 0.5, getTargetSlot().getView().getHeight() * 0.5).getY();
        });

        paintProperty.setBinder((Object connection) -> {
            java.awt.Color color = ((Connection)connection).getSource().getProperty().getType().getColor();
            return new Color(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0, 1.0);
        });
    }

    public NodeSlotController getSourceSlot() {
        return graphController.findNodeSlotController(((Connection)getContext()).getSource());
    }

    public NodeSlotController getTargetSlot() {
        return graphController.findNodeSlotController(((Connection)getContext()).getTarget());
    }
}
