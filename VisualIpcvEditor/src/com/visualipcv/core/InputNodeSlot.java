package com.visualipcv.core;

public class InputNodeSlot extends NodeSlot {
    private Object value;
    private OutputNodeSlot input;

    public InputNodeSlot(Node node, ProcessorProperty property) {
        super(node, property);
        value = property.getType().getDefaultValue();
    }

    public void connect(NodeSlot slot) {
        if(!(slot instanceof OutputNodeSlot)) {
            throw new IllegalArgumentException("Cannot connect input slot to input slot");
        }

        if(slot.getProperty().getType() != getProperty().getType()) {
            throw new IllegalArgumentException("Slot types mismatch");
        }

        if(input != null) {
            disconnect();
        }

        this.input = (OutputNodeSlot)slot;
        getNode().getGraph().addConnectionRecord(slot, this);
    }

    public void disconnect() {
        if(input != null) {
            this.input = null;
        }
        getNode().getGraph().removeConnectionRecords(this);
    }

    public boolean isConnected() {
        return input != null;
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
