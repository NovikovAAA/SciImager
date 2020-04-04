package com.visualipcv.core;

public class InputNodeSlot extends NodeSlot {
    private Object value;

    public InputNodeSlot(Node node, ProcessorProperty property) {
        super(node, property);
        value = property.getType().getDefaultValue();
    }

    public void connect(NodeSlot slot) {
        if(!isConnectionAvailable(this, slot))
            throw new IllegalArgumentException("Connection failed");

        if(getConnectedSlot() != null) {
            disconnect();
        }

        getNode().getGraph().addConnection(new Connection(slot, this));
    }

    public void disconnect() {
        getNode().getGraph().removeConnections(this);
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
