package com.visualipcv;

import com.visualipcv.view.NodeSlotType;

public class Node {
    private Processor processor;
    private NodeSlot[] inputSlots;
    private NodeSlot[] outputSots;
    private int x;
    private int y;

    public Node(Processor processor, int x, int y) {
        this.processor = processor;
        inputSlots = new NodeSlot[processor.getInputPropertyCount()];
        outputSots = new NodeSlot[processor.getOutputPropertyCount()];

        for(int i = 0; i < inputSlots.length; i++) {
            inputSlots[i] = new NodeSlot(this, processor.getInputProperties().get(i), NodeSlotType.INPUT);
        }

        for(int i = 0; i < outputSots.length; i++) {
            outputSots[i] = new NodeSlot(this, processor.getOutputProperties().get(i), NodeSlotType.OUTPUT);
        }

        this.x = x;
        this.y = y;
    }

    public Processor getProcessor() {
        return processor;
    }

    public int getInputSlotCount() {
        return inputSlots.length;
    }

    public int getOutputSlotCount() {
        return outputSots.length;
    }

    public NodeSlot getInputSlot(int index) {
        return inputSlots[index];
    }

    public NodeSlot getOutputSlot(int index) {
        return outputSots[index];
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
