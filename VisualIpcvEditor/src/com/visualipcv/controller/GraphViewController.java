package com.visualipcv.controller;

import com.visualipcv.core.Processor;
import com.visualipcv.view.GraphView;
import com.visualipcv.view.NodeView;

public class GraphViewController {
    private GraphView graphView;

    public GraphViewController(GraphView view) {
        this.graphView = view;
    }

    public void addNode(Processor processor, double x, double y) {
        graphView.getNodes().add(new NodeView(graphView, processor, x, y));
    }
}
