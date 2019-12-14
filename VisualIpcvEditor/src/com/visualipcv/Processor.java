package com.visualipcv;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Processor {
    private String name;
    private Map<String, DataType> inputProperties;
    private Map<String, DataType> outputProperties;

    public Processor(String name, Map<String, DataType> inputProperties, Map<String, DataType> outputProperties) {
        this.name = name;
        this.inputProperties = inputProperties;
        this.outputProperties = outputProperties;
    }

    public String getName() {
        return name;
    }

    public int getInputPropertyCount() {
        return inputProperties.size();
    }

    public int getOutputPropertyCount() {
        return outputProperties.size();
    }

    public DataType getInputPropertyDataType(String name) {
        return inputProperties.get(name);
    }

    public DataType getOutputPropertyDataType(String name) {
        return outputProperties.get(name);
    }

    public Set<String> getInputProperties() {
        return inputProperties.keySet();
    }

    public Set<String> getOutputProperties() {
        return outputProperties.keySet();
    }

    @Override
    public String toString() {
        return name;
    }
}
