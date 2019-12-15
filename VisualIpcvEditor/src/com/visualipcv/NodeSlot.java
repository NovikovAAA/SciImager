package com.visualipcv;

import com.visualipcv.view.NodeSlotType;
import com.visualipcv.view.events.NodeSlotEventListener;

import java.util.ArrayList;

public class NodeSlot {
    private ProcessorProperty property;
    private NodeSlot input;
    private Object value;
    private NodeSlotType type;
    private Node node;

    private ArrayList<NodeSlotEventListener> listeners = new ArrayList<>();

    public NodeSlot(Node node, ProcessorProperty property, NodeSlotType type) {
        this.property = property;
        this.type = type;
        this.node = node;
    }

    public ProcessorProperty getProperty() {
        return property;
    }

    public NodeSlotType getType() {
        return type;
    }

    public void connect(NodeSlot input) {
        if(type != NodeSlotType.INPUT || input.getType() != NodeSlotType.OUTPUT) {
            throw new IllegalArgumentException("connect can be called only for input slot and takes output slot as argument");
        }

        if(this.input != null) {
            throw new IllegalArgumentException("input slot can have only one connection");
        }

        this.input = input;

        for(NodeSlotEventListener listener : listeners) {
            listener.onConnected(input, this);
        }
    }

    public void disconnect() {
        for(NodeSlotEventListener listener : listeners) {
            listener.onDisconnected(input, this);
        }

        this.input = null;
    }

    public NodeSlot getConnectedSlot() {
        return input;
    }

    public void setValue(Object value) {
        if(type != NodeSlotType.INPUT) {
            throw new IllegalArgumentException("setValue is only allowed for input slot");
        }
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public Node getNode() {
        return node;
    }

    public void addNodeSlotEventListener(NodeSlotEventListener listener) {
        listeners.add(listener);
    }

    public void removeNodeSlotEventListener(NodeSlotEventListener listener) {
        listeners.remove(listener);
    }
}
