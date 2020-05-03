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
            return ((Connection)connection).getSource().getActualType().getColor();
        });
    }

    public NodeSlotController getSourceSlot() {
        return graphController.findNodeSlotController(((Connection)getContext()).getSource());
    }

    public NodeSlotController getTargetSlot() {
        return graphController.findNodeSlotController(((Connection)getContext()).getTarget());
    }
}
