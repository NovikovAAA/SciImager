package com.visualipcv.view.events;

import com.visualipcv.Graph;
import com.visualipcv.Node;

public interface GraphModifiedEventListener {
    void onNodeAdded(Graph graph, Node node);
    void onNodeRemoved(Graph graph, Node node);
}
