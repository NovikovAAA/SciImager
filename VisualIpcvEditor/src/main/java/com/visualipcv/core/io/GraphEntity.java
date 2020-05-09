package com.visualipcv.core.io;

import com.visualipcv.core.Connection;
import com.visualipcv.core.Graph;
import com.visualipcv.core.GraphElement;
import com.visualipcv.core.Group;
import com.visualipcv.core.Node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GraphEntity implements Serializable {
    private String name;
    private UUID id;
    private List<NodeEntity> nodes;
    private List<ConnectionEntity> connections;
    private List<GroupEntity> groups;

    public GraphEntity(Graph graph) {
        name = graph.getName();
        nodes = new ArrayList<>();
        connections = new ArrayList<>();
        groups = new ArrayList<>();
        id = graph.getId();

        for(GraphElement node : graph.getNodes()) {
            if(node instanceof Node)
                nodes.add(new NodeEntity((Node)node));
            else
                groups.add(new GroupEntity((Group)node));
        }

        for(Connection connection : graph.getConnections()) {
            connections.add(new ConnectionEntity(connection));
        }
    }

    public GraphEntity(GraphClipboard graph) {
        nodes = new ArrayList<>();
        connections = new ArrayList<>();
        groups = new ArrayList<>();

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

    public UUID getId() {
        return id;
    }

    public List<NodeEntity> getNodes() {
        return nodes;
    }

    public List<ConnectionEntity> getConnections() {
        return connections;
    }

    public List<GroupEntity> getGroups() {
        return groups;
    }
}
