package com.visualipcv.viewmodel;

import com.visualipcv.core.Connection;
import com.visualipcv.core.Graph;
import com.visualipcv.core.Node;
import com.visualipcv.core.Processor;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class GraphViewModel {
    private Graph graph = new Graph();

    private ObservableList<Node> nodes = FXCollections.observableArrayList();
    private ObservableList<Connection> connections = FXCollections.observableArrayList();

    public GraphViewModel() {
        nodes.addListener(new ListChangeListener<Node>() {
            @Override
            public void onChanged(Change<? extends Node> c) {
                while(c.next()) {
                    if(c.wasRemoved()) {
                        for(Node node : c.getRemoved()) {
                            graph.removeNode(node);
                        }
                    } else if(c.wasAdded()) {
                        for(Node node : c.getAddedSubList()) {
                            graph.addNode(node);
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

    public ObservableList<Node> getNodeList() {
        return nodes;
    }

    public ObservableList<Connection> getConnections() {
        return connections;
    }

    public void addNode(Processor processor, double x, double y) {
        Node node = new Node(graph, processor, x, y);
        nodes.add(node);
    }
}
