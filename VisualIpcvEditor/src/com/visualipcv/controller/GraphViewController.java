package com.visualipcv.controller;

import com.visualipcv.core.Processor;
import com.visualipcv.view.GraphView;
import com.visualipcv.view.NodeView;

public class GraphViewController {
    private GraphView graphView;

    public GraphViewController(GraphView view) {
        this.graphView = view;
    }

    public void removeSelected() {
        graphView.getNodes().removeAll(graphView.getSelectedNodes());
    }

    public void moveSelected(double deltaX, double deltaY) {
        graphView.getSelectedNodes().forEach((NodeView view) -> {
            view.setLayoutX(view.getLayoutX() + deltaX / graphView.getZoom());
            view.setLayoutY(view.getLayoutY() + deltaY / graphView.getZoom());
        });
    }

    public void addConnection() {

    }
}
