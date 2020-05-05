package com.visualipcv.core;

import com.visualipcv.Console;
import com.visualipcv.core.io.NodeEntity;
import com.visualipcv.core.io.NodeSlotEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Node extends GraphElement {
    private ProcessorUID processorUID;
    private List<InputNodeSlot> inputSlots;
    private List<OutputNodeSlot> outputSlots;
    private boolean isProxy = false;

    private GraphExecutionException lastError;

    public Node(Graph graph, Processor processor, double x, double y) {
        super(graph, processor.getName(), x, y);
        this.processorUID = processor.getUID();

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
        super(graph, nodeEntity.getName(), nodeEntity.getX(), nodeEntity.getY());

        this.setId(nodeEntity.getId());
        this.processorUID = nodeEntity.getProcessorUID();

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

    public boolean isProxy() {
        return isProxy;
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

    public NodeSlot getNodeSlot(String name) {
        InputNodeSlot inputNodeSlot = getInputNodeSlot(name);

        if(inputNodeSlot != null)
            return inputNodeSlot;

        return getOutputNodeSlot(name);
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

    @Override
    public void onCreate() throws GraphExecutionException {
        try {
            if(findProcessor() != null)
                findProcessor().onCreate();
        } catch (CommonException e) {
            throw new GraphExecutionException(null, e.getMessage());
        }
    }

    @Override
    public void onDestroy() throws GraphExecutionException {
        for(GraphElement n : getGraph().getNodes()) {
            if(n instanceof Node) {
                Node temp = (Node)n;
                for(InputNodeSlot slot : temp.getInputSlots()) {
                    if(slot.getConnectedSlot() == null)
                        continue;

                    if(slot.getConnectedSlot().getNode() == this) {
                        slot.disconnect();
                    }
                }
            }
        }

        for(InputNodeSlot slot : getInputSlots()) {
            slot.disconnect();
        }

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
