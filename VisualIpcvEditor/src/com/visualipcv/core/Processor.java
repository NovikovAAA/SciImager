package com.visualipcv.core;

import java.util.List;

public abstract class Processor {
    private String name;
    private String module;
    private String category;
    private List<ProcessorProperty> inputProperties;
    private List<ProcessorProperty> outputProperties;

    public Processor(String name, String module, String category, List<ProcessorProperty> inputProperties, List<ProcessorProperty> outputProperties) {
        this.name = name;
        this.module = module;
        this.category = category;
        this.inputProperties = inputProperties;
        this.outputProperties = outputProperties;
    }

    public String getName() {
        return name;
    }

    public String getModule() {
        return module;
    }

    public String getCategory() {
        return category;
    }

    public boolean isOutput() {
        return outputProperties.isEmpty();
    }

    private ProcessorProperty getProperty(String name, List<ProcessorProperty> properties) {
        for(ProcessorProperty prop : properties) {
            if(prop.getName().equals(name)) {
                return prop;
            }
        }
        return null;
    }

    public DataType getInputPropertyDataType(String name) {
        return getProperty(name, inputProperties).getType();
    }

    public DataType getOutputPropertyDataType(String name) {
        return getProperty(name, outputProperties).getType();
    }

    public List<ProcessorProperty> getInputProperties() {
        return inputProperties;
    }

    public List<ProcessorProperty> getOutputProperties() {
        return outputProperties;
    }

    @Override
    public String toString() {
        return name;
    }

    public abstract DataBundle execute(DataBundle bundle);
}
