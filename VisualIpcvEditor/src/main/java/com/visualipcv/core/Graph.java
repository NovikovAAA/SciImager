package com.visualipcv.core;

import com.visualipcv.Console;
import com.visualipcv.controller.NodeController;
import com.visualipcv.core.io.ConnectionEntity;
import com.visualipcv.core.io.GraphClipboard;
import com.visualipcv.core.io.GraphEntity;
import com.visualipcv.core.io.NodeEntity;
import com.visualipcv.utils.ProcUtils;
import com.visualipcv.view.CustomDataFormats;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Graph implements IDocumentPart {
    private String name;
    private ArrayList<Node> nodes = new ArrayList<>();
    private Set<Connection> connections = new HashSet<>();
    private Map<NodeSlot, List<Connection>> slotConnectionAssoc = new HashMap<>();
    private Map<Node, Map<String, Object>> cache = new HashMap<>();

    public Graph() {

    }

    public Graph(GraphEntity graphEntity) {
        name = graphEntity.getName();

        for(NodeEntity node : graphEntity.getNodes()) {
            addNode(new Node(this, node));
        }

        for(ConnectionEntity connection : graphEntity.getConnections()) {
            addConnection(new Connection(this, connection));
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void addNode(Node node) {
        nodes.add(node);

        try {
            node.onCreate();
        } catch (GraphExecutionException e) {
            Console.write(e.getMessage());
        }
    }

    public void addNodes(Collection<Node> nodes) {
        this.nodes.addAll(nodes);

        for(Node node : nodes) {
            try {
                node.onCreate();
            } catch (GraphExecutionException e) {
                Console.write(e.getMessage());
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
            Console.write(e.getMessage());
        }
    }

    public void addConnection(Connection connection) {
        connections.add(connection);
        slotConnectionAssoc.computeIfAbsent(connection.getSource(), k -> new ArrayList<>());
        slotConnectionAssoc.computeIfAbsent(connection.getTarget(), k -> new ArrayList<>());
        slotConnectionAssoc.get(connection.getSource()).add(connection);
        slotConnectionAssoc.get(connection.getTarget()).add(connection);
        updateProperties(connection.getSource().getNode(), new HashSet<>());
    }

    public void removeConnections(NodeSlot slot) {
        List<Connection> connectionsToRemove = new ArrayList<>();

        for(Connection connection : connections) {
            if(connection.getSource() == slot || connection.getTarget() == slot) {
                connectionsToRemove.add(connection);
            }
        }

        for(Connection connection : connectionsToRemove) {
            slotConnectionAssoc.get(connection.getSource()).remove(connection);
            slotConnectionAssoc.get(connection.getTarget()).remove(connection);

            HashSet<Node> updatedNodes = new HashSet<>();
            updateProperties(connection.getSource().getNode(), updatedNodes);
            updateProperties(connection.getTarget().getNode(), updatedNodes);
        }

        connections.removeAll(connectionsToRemove);
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public Set<Connection> getConnections() {
        return connections;
    }

    public List<Connection> getConnections(NodeSlot slot) {
        slotConnectionAssoc.computeIfAbsent(slot, nodeSlot -> new ArrayList<>());
        return slotConnectionAssoc.get(slot);
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

    public DataType getTypeOfCalculatedSlot(Node node, String name) {
        if(!cache.containsKey(node))
            return null;

        return node.getOutputNodeSlot(name).getActualType();
    }

    private void updateProperties(Node node, Set<Node> updatedNodes) {
        if(updatedNodes.contains(node))
            return;

        ProcUtils.shareType(node);
        updatedNodes.add(node);

        for(InputNodeSlot slot : node.getInputSlots()) {
            for(Connection connection : getConnections(slot)) {
                if(connection.getSource().getNode() != node)
                    updateProperties(connection.getSource().getNode(), updatedNodes);
                if(connection.getTarget().getNode() != node)
                    updateProperties(connection.getTarget().getNode(), updatedNodes);
            }
        }

        for(NodeSlot slot : node.getOutputSlots()) {
            for(Connection connection : getConnections(slot)) {
                if(connection.getSource().getNode() != node)
                    updateProperties(connection.getSource().getNode(), updatedNodes);
                if(connection.getTarget().getNode() != node)
                    updateProperties(connection.getTarget().getNode(), updatedNodes);
            }
        }
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

    @Override
    public Object getSerializableProxy() {
        return new GraphEntity(this);
    }
}
