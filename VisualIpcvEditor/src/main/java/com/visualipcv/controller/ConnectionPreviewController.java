package com.visualipcv.controller;

import com.visualipcv.core.NodeSlot;

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
            return ((NodeSlot)slot).getActualType().getColor();
        });

        initialize();
    }
}
