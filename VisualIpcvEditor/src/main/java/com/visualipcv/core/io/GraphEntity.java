package com.visualipcv.core.io;

import com.visualipcv.core.Connection;
import com.visualipcv.core.Graph;
import com.visualipcv.core.Node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GraphEntity implements Serializable {
    private String name;
    private List<NodeEntity> nodes;
    private List<ConnectionEntity> connections;

    public GraphEntity(Graph graph) {
        name = graph.getName();
        nodes = new ArrayList<>();
        connections = new ArrayList<>();

        for(Node node : graph.getNodes()) {
            nodes.add(new NodeEntity(node));
        }

        for(Connection connection : graph.getConnections()) {
            connections.add(new ConnectionEntity(connection));
        }
    }

    public GraphEntity(GraphClipboard graph) {
        nodes = new ArrayList<>();
        connections = new ArrayList<>();

        for(Node node : graph.getNodes()) {
            nodes.add(new NodeEntity(node));
        }

        for(Connection connection : graph.getConnections()) {
            connections.add(new ConnectionEntity(connection));
        }
    }

    public String getName() {
        return name;
    }

    public List<NodeEntity> getNodes() {
        return nodes;
    }

    public List<ConnectionEntity> getConnections() {
        return connections;
    }
}
