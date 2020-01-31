package com.visualipcv.core;

import com.visualipcv.view.events.NodeSlotEventListener;

import java.util.ArrayList;

public abstract class NodeSlot {
    private ProcessorProperty property;
    private Node node;

    private ArrayList<NodeSlotEventListener> listeners = new ArrayList<>();

    public NodeSlot(Node node, ProcessorProperty property) {
        this.property = property;
        this.node = node;
    }

    public ProcessorProperty getProperty() {
        return property;
    }

    public abstract void connect(NodeSlot other);
    public abstract void disconnect();

    public Node getNode() {
        return node;
    }

    public void addNodeSlotEventListener(NodeSlotEventListener listener) {
        listeners.add(listener);
    }

    public void removeNodeSlotEventListener(NodeSlotEventListener listener) {
        listeners.remove(listener);
    }

    protected void onConnected(NodeSlot other) {
        for(NodeSlotEventListener listener : listeners) {
            listener.onConnected(other, this);
        }
    }

    protected void onDisconnected(NodeSlot other) {
        for(NodeSlotEventListener listener : listeners) {
            listener.onDisconnected(other, this);
        }
    }
}
