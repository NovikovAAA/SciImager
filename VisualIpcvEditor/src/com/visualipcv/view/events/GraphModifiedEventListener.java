package com.visualipcv.view.events;

import com.visualipcv.core.Graph;
import com.visualipcv.core.Node;

public interface GraphModifiedEventListener {
    void onNodeAdded(Graph graph, Node node);
    void onNodeRemoved(Graph graph, Node node);
}
