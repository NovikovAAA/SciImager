package com.visualipcv.viewmodel;

import com.visualipcv.controller.GraphViewController;
import com.visualipcv.core.Connection;
import com.visualipcv.core.Graph;
import com.visualipcv.core.Node;
import com.visualipcv.core.Processor;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.List;

public class GraphViewModel {
    private Graph graph = new Graph();
    private GraphViewController controller;

    private ObservableList<NodeViewModel> nodes = FXCollections.observableArrayList();
    private ObservableList<Connection> connections = FXCollections.observableArrayList();

    public GraphViewModel(GraphViewController controller) {
        this.controller = controller;

        nodes.addListener(new ListChangeListener<NodeViewModel>() {
            @Override
            public void onChanged(Change<? extends NodeViewModel> c) {
                while(c.next()) {
                    if(c.wasAdded()) {
                        for(NodeViewModel vm : c.getAddedSubList()) {
                            graph.addNode(vm.getNode());
                        }
                    }
                    if(c.wasRemoved()) {
                        for(NodeViewModel vm : c.getRemoved()) {
                            graph.removeNode(vm.getNode());
                        }
                    }
                }
            }
        });
    }

    public void updateConnections() {
        connections.clear();
        connections.addAll(graph.getConnections());
    }

    public List<NodeViewModel> getNodes() {
        return nodes;
    }

    public Graph getGraph() {
        return graph;
    }

    public void addNode(Processor processor, double x, double y) {
        controller.addNode(processor, x, y);
    }
}
