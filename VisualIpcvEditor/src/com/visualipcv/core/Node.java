package com.visualipcv.core;

import com.visualipcv.Console;
import com.visualipcv.core.io.NodeEntity;
import org.opencv.core.CvException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Node {
    private final UUID id;
    private Graph graph;
    private Processor processor;
    private List<InputNodeSlot> inputSlots;
    private List<OutputNodeSlot> outputSlots;
    private DataBundle state = new DataBundle();
    private double x;
    private double y;

    private GraphExecutionException lastError;

    public Node(Graph graph, Processor processor, double x, double y) {
        id = java.util.UUID.randomUUID();
        initNode(graph, processor, x, y);
    }

    public Node(Graph graph, NodeEntity nodeEntity) {
        id = nodeEntity.getId();
        Processor processor = ProcessorLibrary.findProcessor(nodeEntity.getProcessorUID().getModule(), nodeEntity.getProcessorUID().getName());
        initNode(graph, processor, nodeEntity.getX(), nodeEntity.getY());

        for(InputNodeSlot slot : inputSlots) {
            if(nodeEntity.getInputValues().containsKey(slot.getProperty().getName())) {
                try {
                    slot.setValue(nodeEntity.getInputValues().get(slot.getProperty().getName()));
                } catch (ValidationException e) {
                    Console.output(e.getMessage());
                }
            }
        }
    }

    private void initNode(Graph graph, Processor processor, double x, double y) {
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

    public UUID getId() {
        return id;
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

    public InputNodeSlot getInputNodeSlot(String name) {
        for(InputNodeSlot slot : inputSlots) {
            if(slot.getProperty().getName().equals(name))
                return slot;
        }
        return null;
    }

    public OutputNodeSlot getOutputNodeSlot(String name) {
        for(OutputNodeSlot slot : outputSlots) {
            if(slot.getProperty().getName().equals(name))
                return slot;
        }
        return null;
    }

    public NodeSlot getNodeSlot(String name) {
        InputNodeSlot inputNodeSlot = getInputNodeSlot(name);

        if(inputNodeSlot != null)
            return inputNodeSlot;

        return getOutputNodeSlot(name);
    }

    public DataBundle getState() {
        return state;
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

    public GraphExecutionException getLastError() {
        return lastError;
    }

    public void execute() throws GraphExecutionException {
        lastError = null;
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
            lastError = new GraphExecutionException(this, e.getMessage());
            throw lastError;
        }

        for (OutputNodeSlot outputSlot : outputSlots) {
            graph.writeCache(this, outputSlot.getProperty().getName(), res.read(outputSlot.getProperty().getName()));
        }
    }

    public void onCreate() throws GraphExecutionException {
        try {
            processor.onCreated(state);
        } catch (CommonException e) {
            throw new GraphExecutionException(null, e.getMessage());
        }
    }

    public void onDestroy() throws GraphExecutionException {
        try {
            processor.onDestroyed(state);
        } catch (CommonException e) {
            throw new GraphExecutionException(null, e.getMessage());
        }
    }
}
