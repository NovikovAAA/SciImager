package com.visualipcv.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
    private ArrayList<Node> nodes = new ArrayList<>();
    private Map<Node, Map<String, Object>> cache = new HashMap<>();

    public Graph() {

    }

    public void addNode(Node node) {
        nodes.add(node);
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
    }

    public List<Node> getNodes() {
        return nodes;
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

    public void execute() {
        cache.clear();
        List<Node> nodes = getOutputNodes();

        for (Node node : nodes) {
            node.execute();
        }
    }
}
