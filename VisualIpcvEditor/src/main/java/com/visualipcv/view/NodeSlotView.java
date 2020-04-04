package com.visualipcv.view;

import com.visualipcv.controller.ConnectionController;
import com.visualipcv.controller.NodeSlotController;
import javafx.scene.layout.AnchorPane;

import java.util.List;

public class NodeSlotView extends AnchorPane {
    private NodeSlotController controller;

    public void setController(NodeSlotController controller) {
        this.controller = controller;
    }

    @Override
    public void layoutChildren() {
        super.layoutChildren();

        for(ConnectionController connection : controller.getConnections()) {
            connection.invalidate();
        }
    }
}
