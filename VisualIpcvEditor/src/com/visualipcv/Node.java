package com.visualipcv;

import com.visualipcv.view.NodeSlotType;

import java.util.ArrayList;
import java.util.List;

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

        for(int i = 0; i < processor.getInputPropertyCount(); i++) {
            inputSlots.add(new InputNodeSlot(this, processor.getInputProperties().get(i), 0));
        }

        for(int i = 0; i < processor.getOutputPropertyCount(); i++) {
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
}
