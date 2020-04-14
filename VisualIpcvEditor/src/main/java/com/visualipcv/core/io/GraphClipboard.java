package com.visualipcv.core.io;

import com.visualipcv.core.Connection;
import com.visualipcv.core.Graph;
import com.visualipcv.core.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GraphClipboard {
    private Collection<Node> nodes = new ArrayList<>();
    private Collection<Connection> connections = new ArrayList<>();

    public GraphClipboard(Collection<Node> nodes, Collection<Connection> connections) {
        this.nodes = nodes;
        this.connections = connections;
    }

    public GraphClipboard(Graph graph, GraphEntity entity) {
        Map<UUID, UUID> uidMapping = new HashMap<>();

        for(NodeEntity node : entity.getNodes()) {
            UUID oldId = node.getId();
            node.resetUID();
            UUID newId = node.getId();

            nodes.add(new Node(graph, node));
            uidMapping.put(oldId, newId);
        }

        for(ConnectionEntity connection : entity.getConnections()) {
            UUID newSourceId = uidMapping.get(connection.getSourceNodeId());
            UUID newTargetId = uidMapping.get(connection.getTargetNodeId());
            connection.updateUIDs(newSourceId, newTargetId);
            connections.add(new Connection(graph, connection));
        }
    }

    public Collection<Node> getNodes() {
        return nodes;
    }

    public Collection<Connection> getConnections() {
        return connections;
    }
}