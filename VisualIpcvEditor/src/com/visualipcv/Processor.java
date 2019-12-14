package com.visualipcv;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Processor {
    private String name;
    private String category;
    private Map<String, DataType> inputProperties;
    private Map<String, DataType> outputProperties;

    public Processor(String name, String category, Map<String, DataType> inputProperties, Map<String, DataType> outputProperties) {
        this.name = name;
        this.category = category;
        this.inputProperties = inputProperties;
        this.outputProperties = outputProperties;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
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

    public Map<String, DataType> getInputProperties() {
        return inputProperties;
    }

    public Map<String, DataType> getOutputProperties() {
        return outputProperties;
    }

    @Override
    public String toString() {
        return name;
    }
}
