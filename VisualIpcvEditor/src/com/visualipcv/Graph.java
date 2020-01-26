package com.visualipcv;

import com.visualipcv.view.events.GraphModifiedEventListener;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private ArrayList<Node> nodes = new ArrayList<>();
    private ArrayList<GraphModifiedEventListener> listeners = new ArrayList<>();

    public Graph() {

    }

    public void addNode(Node node) {
        nodes.add(node);

        for(GraphModifiedEventListener listener : listeners) {
            listener.onNodeAdded(this, node);
        }
    }

    public void removeNode(Node node) {
        nodes.remove(node);

        for(GraphModifiedEventListener listener : listeners) {
            listener.onNodeRemoved(this, node);
        }
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void addGraphEventListener(GraphModifiedEventListener listener) {
        listeners.add(listener);
    }

    public void removeGraphEventListener(GraphModifiedEventListener listener) {
        listeners.remove(listener);
    }
}
