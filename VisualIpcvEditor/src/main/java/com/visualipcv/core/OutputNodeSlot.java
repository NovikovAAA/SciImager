package com.visualipcv.core;

import com.visualipcv.core.io.NodeSlotEntity;

public class OutputNodeSlot extends NodeSlot {
    public OutputNodeSlot(Node node, ProcessorProperty property) {
        super(node, property);
    }

    public OutputNodeSlot(Node node, NodeSlotEntity entity) {
        super(node, entity);
    }

    public void connect(NodeSlot slot) {
        if(!(slot instanceof InputNodeSlot)) {
            throw new IllegalArgumentException("Output slot can be only connected to input slot");
        }

        slot.connect(this);
    }

    public void disconnect() {
        for(GraphElement n : getNode().getGraph().getNodes()) {
            if(!(n instanceof Node))
                continue;

            Node node = (Node)n;

            if(node == getNode()) {
                continue;
            }

            for(InputNodeSlot is : node.getInputSlots()) {
                if(is.getConnectedSlot() == this) {
                    is.disconnect();
                }
            }
        }
    }

    public boolean isConnected() {
        for(GraphElement n : getNode().getGraph().getNodes()) {
            if(!(n instanceof Node))
                continue;

            Node node = (Node)n;

            if(node == getNode()) {
                continue;
            }

            for(InputNodeSlot is : node.getInputSlots()) {
                if(is.getConnectedSlot() == this) {
                    return true;
                }
            }
        }

        return false;
    }
}
