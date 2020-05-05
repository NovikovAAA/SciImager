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
        for(Node n : getNode().getGraph().getNodes()) {
            if(n == getNode()) {
                continue;
            }

            for(InputNodeSlot is : n.getInputSlots()) {
                if(is.getConnectedSlot() == this) {
                    is.disconnect();
                }
            }
        }
    }

    public boolean isConnected() {
        for(Node n : getNode().getGraph().getNodes()) {
            if(n == getNode()) {
                continue;
            }

            for(InputNodeSlot is : n.getInputSlots()) {
                if(is.getConnectedSlot() == this) {
                    return true;
                }
            }
        }

        return false;
    }
}
