package com.visualipcv.core;

import java.util.ArrayList;

public abstract class NodeSlot {
    private ProcessorProperty property;
    private Node node;

    public NodeSlot(Node node, ProcessorProperty property) {
        this.property = property;
        this.node = node;
    }

    public ProcessorProperty getProperty() {
        return property;
    }

    public Node getNode() {
        return node;
    }

    public abstract void connect(NodeSlot other);
    public abstract void disconnect();
    public abstract boolean isConnected();
}
