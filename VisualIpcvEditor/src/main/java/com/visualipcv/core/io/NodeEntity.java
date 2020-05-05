package com.visualipcv.core.io;

import com.visualipcv.core.InputNodeSlot;
import com.visualipcv.core.Node;
import com.visualipcv.core.OutputNodeSlot;
import com.visualipcv.core.ProcessorUID;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class NodeEntity implements Serializable {
    private UUID id;
    private ProcessorUID processorUID;
    private Map<String, Object> inputValues;
    private double x;
    private double y;
    private String name;

    private List<NodeSlotEntity> inputSlots = new ArrayList<>();
    private List<NodeSlotEntity> outputSlots = new ArrayList<>();

    public NodeEntity(Node node) {
        this.id = node.getId();
        this.x = node.getX();
        this.y = node.getY();
        this.inputValues = new HashMap<>();
        this.name = node.getName();
        this.processorUID = node.getProcessorUID();

        for(InputNodeSlot slot : node.getInputSlots()) {
            inputSlots.add(new NodeSlotEntity(slot));
        }

        for(OutputNodeSlot slot : node.getOutputSlots()) {
            outputSlots.add(new NodeSlotEntity(slot));
        }

        for(InputNodeSlot slot : node.getInputSlots()) {
            inputValues.put(slot.getProperty().getName(), slot.getValue());
        }
    }

    public void resetUID() {
        id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public ProcessorUID getProcessorUID() {
        return processorUID;
    }

    public Map<String, Object> getInputValues() {
        return inputValues;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    public void addOffset(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    public List<NodeSlotEntity> getInputSlots() {
        return inputSlots;
    }

    public List<NodeSlotEntity> getOutputSlots() {
        return outputSlots;
    }
}
