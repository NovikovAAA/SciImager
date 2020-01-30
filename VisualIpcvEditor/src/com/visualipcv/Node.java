package com.visualipcv;

import com.visualipcv.view.NodeSlotType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Node {
    private Graph graph;
    private Processor processor;
    private List<InputNodeSlot> inputSlots;
    private List<OutputNodeSlot> outputSots;
    private int x;
    private int y;

    public Node(Graph graph, Processor processor, int x, int y) {
        this.graph = graph;
        this.processor = processor;
        inputSlots = new ArrayList<>();
        outputSots = new ArrayList<>();

        for(int i = 0; i < processor.getInputProperties().size(); i++) {
            inputSlots.add(new InputNodeSlot(this, processor.getInputProperties().get(i)));
        }

        for(int i = 0; i < processor.getOutputProperties().size(); i++) {
            outputSots.add(new OutputNodeSlot(this, processor.getOutputProperties().get(i)));
        }

        this.x = x;
        this.y = y;
    }

    public Graph getGraph() {
        return graph;
    }

    public Processor getProcessor() {
        return processor;
    }

    public List<InputNodeSlot> getInputSlots() {
        return inputSlots;
    }

    public List<OutputNodeSlot> getOutputSots() {
        return outputSots;
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    private Object getValueFromInput(InputNodeSlot slot) {
        if(slot.getConnectedSlot() != null) {
            Object fromCache = graph.readCache(slot.getConnectedSlot().getNode(), slot.getConnectedSlot().getProperty().getName());

            if(fromCache == null) {
                slot.getConnectedSlot().getNode().execute();
            }

            return graph.readCache(slot.getConnectedSlot().getNode(), slot.getConnectedSlot().getProperty().getName());
        }

        return slot.getValue();
    }

    public void execute() {
        List<Object> inputs = new ArrayList<>();

        for(InputNodeSlot slot : inputSlots) {
            inputs.add(getValueFromInput(slot));
        }

        List<Object> res = processor.execute(inputs);

        for(int i = 0; i < res.size(); i++) {
            graph.writeCache(this, outputSots.get(i).getProperty().getName(), res.get(i));
        }
    }
}
