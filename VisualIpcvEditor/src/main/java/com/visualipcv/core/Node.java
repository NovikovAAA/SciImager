package com.visualipcv.core;

import com.visualipcv.Console;
import com.visualipcv.core.io.NodeEntity;
import com.visualipcv.core.io.NodeSlotEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Node {
    private final UUID id;
    private Graph graph;
    private ProcessorUID processorUID;
    private String name = "Property";
    private List<InputNodeSlot> inputSlots;
    private List<OutputNodeSlot> outputSlots;
    private double x;
    private double y;
    private List<NodeCommand> commands = new ArrayList<>();
    private boolean isProxy = false;

    private GraphExecutionException lastError;

    public Node(Graph graph, Processor processor, double x, double y) {
        this.id = java.util.UUID.randomUUID();
        this.name = processor.getName();
        this.processorUID = processor.getUID();
        this.graph = graph;

        this.x = x;
        this.y = y;

        this.inputSlots = new ArrayList<>();
        this.outputSlots = new ArrayList<>();

        for(int i = 0; i < processor.getInputProperties().size(); i++) {
            inputSlots.add(new InputNodeSlot(this, processor.getInputProperties().get(i)));
        }

        for(int i = 0; i < processor.getOutputProperties().size(); i++) {
            outputSlots.add(new OutputNodeSlot(this, processor.getOutputProperties().get(i)));
        }
    }

    public Node(Graph graph, NodeEntity nodeEntity) {
        this.id = nodeEntity.getId();
        this.name = nodeEntity.getName();
        this.processorUID = nodeEntity.getProcessorUID();
        this.graph = graph;

        this.x = nodeEntity.getX();
        this.y = nodeEntity.getY();

        this.inputSlots = new ArrayList<>();
        this.outputSlots = new ArrayList<>();

        for(NodeSlotEntity entity : nodeEntity.getInputSlots()) {
            inputSlots.add(new InputNodeSlot(this, entity));
        }

        for(NodeSlotEntity entity : nodeEntity.getOutputSlots()) {
            outputSlots.add(new OutputNodeSlot(this, entity));
        }

        for(InputNodeSlot slot : inputSlots) {
            if(nodeEntity.getInputValues().containsKey(slot.getProperty().getName())) {
                try {
                    slot.setValue(nodeEntity.getInputValues().get(slot.getProperty().getName()));
                } catch (ValidationException e) {
                    Console.write(e.getMessage());
                }
            }
        }
    }

    private void checkProcessorCompatibilityHelper() {
        if(isProxy)
            return;

        Processor processor = ProcessorLibrary.findProcessor(processorUID);

        if(processor == null) {
            isProxy = true;
            throw new ProcessorVersionMismatchException(this, processorUID);
        }

        if(processor.getInputProperties().size() != inputSlots.size())
            throw new ProcessorVersionMismatchException(this, processorUID);

        if(processor.getOutputProperties().size() != outputSlots.size())
            throw new ProcessorVersionMismatchException(this, processorUID);

        for(int i = 0; i < processor.getInputProperties().size(); i++) {
            if(!inputSlots.get(i).getProperty().equals(processor.getInputProperties().get(i)))
                throw new ProcessorVersionMismatchException(this, processorUID);
        }

        for(int i = 0; i < processor.getOutputProperties().size(); i++) {
            if(!outputSlots.get(i).getProperty().equals(processor.getOutputProperties().get(i)))
                throw new ProcessorVersionMismatchException(this, processorUID);
        }
    }

    public void checkProcessorCompatibility() {
        try {
            checkProcessorCompatibilityHelper();
        } catch (ProcessorVersionMismatchException e) {
            isProxy = true;
            throw e;
        }
    }

    public UUID getId() {
        return id;
    }

    public boolean isProxy() {
        return isProxy;
    }

    public Graph getGraph() {
        return graph;
    }

    public Processor findProcessor() {
        return ProcessorLibrary.findProcessor(processorUID);
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

    public List<NodeCommand> getCommands() {
        return commands;
    }

    public NodeSlot getNodeSlot(String name) {
        InputNodeSlot inputNodeSlot = getInputNodeSlot(name);

        if(inputNodeSlot != null)
            return inputNodeSlot;

        return getOutputNodeSlot(name);
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

    private Object getValueFromInput(GraphExecutionContext context, InputNodeSlot slot) throws GraphExecutionException {
        if(slot.getConnectedSlot() != null) {
            Object fromCache = GraphExecutionData.load(slot.getConnectedSlot());

            if(fromCache == null) {
                slot.getConnectedSlot().getNode().execute(context);
                fromCache = GraphExecutionData.load(slot.getConnectedSlot());
            }

            DataType dataType = slot.getConnectedType();

            if(fromCache == null) {
                return null;
            }

            if(dataType != slot.getActualType()) {
                if(Converter.isConvertible(dataType, slot.getActualType())) {
                    fromCache = Converter.convert(dataType, slot.getActualType(), fromCache);
                } else {
                    lastError = new GraphExecutionException(this, "Type mismatch: " + dataType.getName() + "/" + slot.getActualType().getName());
                    throw lastError;
                }
            }

            GraphExecutionData.store(slot, fromCache);
            return fromCache;
        }

        return slot.getValue();
    }

    public GraphExecutionException getLastError() {
        return lastError;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        getGraph().onChanged();
    }

    public void execute(GraphExecutionContext context) throws GraphExecutionException {
        Processor processor = ProcessorLibrary.findProcessor(processorUID);

        if(processor == null)
            return;

        lastError = null;
        DataBundle inputs = new DataBundle();

        for(InputNodeSlot slot : inputSlots) {
            inputs.write(slot.getProperty().getName(), getValueFromInput(context, slot));
        }

        DataBundle res;

        try {
            processor.preExecute(context.load(this));
            res = processor.execute(inputs, context.load(this));
            processor.postExecute(context.load(this));
        } catch (Exception e) {
            lastError = new GraphExecutionException(this, e.getMessage());
            throw lastError;
        }

        for (OutputNodeSlot outputSlot : outputSlots) {
            GraphExecutionData.store(outputSlot, res.read(outputSlot.getProperty().getName()));
        }
    }

    public void addCommand(NodeCommand command) {
        this.commands.add(command);
    }

    public void onCreate() throws GraphExecutionException {
        try {
            if(findProcessor() != null)
                findProcessor().onCreate();
        } catch (CommonException e) {
            throw new GraphExecutionException(null, e.getMessage());
        }
    }

    public void onDestroy() throws GraphExecutionException {
        try {
            if(findProcessor() != null)
                findProcessor().onCreate();
        } catch (CommonException e) {
            throw new GraphExecutionException(null, e.getMessage());
        }
    }

    public ProcessorUID getProcessorUID() {
        return processorUID;
    }
}
