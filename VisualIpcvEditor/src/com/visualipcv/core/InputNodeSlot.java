package com.visualipcv.core;

public class InputNodeSlot extends NodeSlot {
    private Object value;

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

        if(slot.getNode().getGraph() != getNode().getGraph()) {
            throw new IllegalArgumentException("Nodes from different graphs cannot be connected");
        }

        if(getConnectedSlot() != null) {
            disconnect();
        }

        getNode().getGraph().addConnectionRecord(new Connection(slot, this));
    }

    public void disconnect() {
        getNode().getGraph().removeConnectionRecords(this);
    }

    public boolean isConnected() {
        return getConnectedSlot() != null;
    }

    public OutputNodeSlot getConnectedSlot() {
        for(Connection connection : getNode().getGraph().getConnections()) {
            if(connection.getTarget() == this)
                return (OutputNodeSlot)connection.getSource();
        }
        return null;
    }

    public void setValue(Object value) throws ValidationException {
        this.value = getProperty().getType().validate(value);
    }

    public Object getValue() {
        return value;
    }
}
