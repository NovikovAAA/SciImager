package com.visualipcv.core;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private Graph graph;
    private Processor processor;
    private List<InputNodeSlot> inputSlots;
    private List<OutputNodeSlot> outputSlots;
    private int x;
    private int y;

    public Node(Graph graph, Processor processor, int x, int y) {
        this.graph = graph;
        this.processor = processor;
        inputSlots = new ArrayList<>();
        outputSlots = new ArrayList<>();

        for(int i = 0; i < processor.getInputProperties().size(); i++) {
            inputSlots.add(new InputNodeSlot(this, processor.getInputProperties().get(i)));
        }

        for(int i = 0; i < processor.getOutputProperties().size(); i++) {
            outputSlots.add(new OutputNodeSlot(this, processor.getOutputProperties().get(i)));
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

    public List<OutputNodeSlot> getOutputSlots() {
        return outputSlots;
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
        DataBundle inputs = new DataBundle();

        for(InputNodeSlot slot : inputSlots) {
            inputs.write(slot.getProperty().getName(), getValueFromInput(slot));
        }

        DataBundle res = processor.execute(inputs);

        for (OutputNodeSlot outputSlot : outputSlots) {
            graph.writeCache(this, outputSlot.getProperty().getName(), res.read(outputSlot.getProperty().getName()));
        }
    }
}
