package com.visualipcv;

import com.visualipcv.view.NodeSlotType;
import com.visualipcv.view.events.NodeSlotEventListener;

public class InputNodeSlot extends NodeSlot {
    private Object value;
    private OutputNodeSlot input;

    public InputNodeSlot(Node node, ProcessorProperty property, Object value) {
        super(node, property);
        this.value = value;
    }

    public void connect(NodeSlot slot) {
        if(!(slot instanceof OutputNodeSlot)) {
            throw new IllegalArgumentException("Cannot connect input slot to input slot");
        }

        if(input != null) {
            throw new IllegalArgumentException("Input slot can be connected to only one other slot");
        }

        if(slot.getProperty().getType() != getProperty().getType()) {
            throw new IllegalArgumentException("Slot types mismatch");
        }

        this.input = (OutputNodeSlot)slot;
        onConnected(slot);
    }

    public void disconnect() {
        onDisconnected(input);
        this.input = null;
    }

    public OutputNodeSlot getConnectedSlot() {
        return input;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}
