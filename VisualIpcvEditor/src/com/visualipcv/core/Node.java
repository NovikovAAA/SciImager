package com.visualipcv.core;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private final Graph graph;
    private final Processor processor;
    private final List<InputNodeSlot> inputSlots;
    private final List<OutputNodeSlot> outputSlots;
    private final DataBundle state = new DataBundle();
    private double x;
    private double y;

    public Node(Graph graph, Processor processor, double x, double y) {
        this.graph = graph;
        this.processor = processor;
        inputSlots = new ArrayList<>();
        outputSlots = new ArrayList<>();

        for(int i = 0; i < this.processor.getInputProperties().size(); i++) {
            inputSlots.add(new InputNodeSlot(this, this.processor.getInputProperties().get(i)));
        }

        for(int i = 0; i < this.processor.getOutputProperties().size(); i++) {
            outputSlots.add(new OutputNodeSlot(this, this.processor.getOutputProperties().get(i)));
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

    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    private Object getValueFromInput(InputNodeSlot slot) throws GraphExecutionException {
        if(slot.getConnectedSlot() != null) {
            Object fromCache = graph.readCache(slot.getConnectedSlot().getNode(), slot.getConnectedSlot().getProperty().getName());

            if(fromCache == null) {
                slot.getConnectedSlot().getNode().execute();
            }

            return graph.readCache(slot.getConnectedSlot().getNode(), slot.getConnectedSlot().getProperty().getName());
        }

        return slot.getValue();
    }

    public void execute() throws GraphExecutionException {
        DataBundle inputs = new DataBundle();

        for(InputNodeSlot slot : inputSlots) {
            inputs.write(slot.getProperty().getName(), getValueFromInput(slot));
        }

        DataBundle res = null;

        try {
            processor.preExecute(state);
            res = processor.execute(inputs, state);
            processor.postExecute(state);
        } catch (Exception e) {
            throw new GraphExecutionException(this, e.getMessage());
        }

        for (OutputNodeSlot outputSlot : outputSlots) {
            graph.writeCache(this, outputSlot.getProperty().getName(), res.read(outputSlot.getProperty().getName()));
        }
    }

    public void onCreated() throws GraphExecutionException {
        try {
            processor.onCreated(state);
        } catch (CommonException e) {
            throw new GraphExecutionException(null, e.getMessage());
        }
    }

    public void onDestroyed() throws GraphExecutionException {
        try {
            processor.onDestroyed(state);
        } catch (CommonException e) {
            throw new GraphExecutionException(null, e.getMessage());
        }
    }
}
