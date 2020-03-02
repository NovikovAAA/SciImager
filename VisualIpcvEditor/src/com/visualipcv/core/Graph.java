package com.visualipcv.core;

import com.visualipcv.Console;
import com.visualipcv.core.io.ConnectionEntity;
import com.visualipcv.core.io.GraphEntity;
import com.visualipcv.core.io.NodeEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Graph {
    private ArrayList<Node> nodes = new ArrayList<>();
    private Set<Connection> connections = new HashSet<>();
    private Map<Node, Map<String, Object>> cache = new HashMap<>();

    public Graph() {

    }

    public Graph(GraphEntity graphEntity) {
        for(NodeEntity node : graphEntity.getNodes()) {
            addNode(new Node(this, node));
        }

        for(ConnectionEntity connection : graphEntity.getConnections()) {
            addConnectionRecord(new Connection(this, connection));
        }
    }

    public void addNode(Node node) {
        nodes.add(node);

        try {
            node.onCreate();
        } catch (GraphExecutionException e) {
            Console.output(e.getMessage());
        }
    }

    public void addNodes(Collection<Node> nodes) {
        this.nodes.addAll(nodes);

        for(Node node : nodes) {
            try {
                node.onCreate();
            } catch (GraphExecutionException e) {
                Console.output(e.getMessage());
            }
        }
    }

    public void removeNode(Node node) {
        nodes.remove(node);

        for(Node n : nodes) {
            for(InputNodeSlot slot : n.getInputSlots()) {
                if(slot.getConnectedSlot() == null)
                    continue;

                if(slot.getConnectedSlot().getNode() == node) {
                    slot.disconnect();
                }
            }
        }

        for(InputNodeSlot slot : node.getInputSlots()) {
            slot.disconnect();
        }

        try {
            node.onDestroy();
        } catch (GraphExecutionException e) {
            Console.output(e.getMessage());
        }
    }

    public void addConnectionRecord(Connection connection) {
        connections.add(connection);
    }

    public void removeConnectionRecords(NodeSlot slot) {
        List<Connection> connectionsToRemove = new ArrayList<>();

        for(Connection connection : connections) {
            if(connection.getSource() == slot || connection.getTarget() == slot) {
                connectionsToRemove.add(connection);
            }
        }

        connections.removeAll(connectionsToRemove);
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public Set<Connection> getConnections() {
        return connections;
    }

    public List<Node> getOutputNodes() {
        List<Node> outputs = new ArrayList<>();

        for(Node node : nodes) {
            if(node.getProcessor().isOutput()) {
                outputs.add(node);
            }
        }

        return outputs;
    }

    public void writeCache(Node node, String name, Object value) {
        Map<String, Object> values;

        if(cache.containsKey(node))
            values = cache.get(node);
        else
            values = new HashMap<>();

        values.put(name, value);
        cache.put(node, values);
    }

    public Object readCache(Node node, String name) {
        if(!cache.containsKey(node))
            return null;

        return cache.get(node).get(name);
    }

    public void execute() throws GraphExecutionException {
        cache.clear();
        List<Node> nodes = getOutputNodes();

        for (Node node : nodes) {
            node.execute();
        }
    }

    public Node findNode(UUID id) {
        for(Node node : nodes) {
            if(node.getId().equals(id)) {
                return node;
            }
        }
        return null;
    }
}
