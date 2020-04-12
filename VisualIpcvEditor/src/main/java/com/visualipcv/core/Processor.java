package com.visualipcv.core;

import java.util.List;

public abstract class Processor {
    private String name;
    private String module;
    private String category;
    private List<ProcessorProperty> inputProperties;
    private List<ProcessorProperty> outputProperties;
    private List<ProcessorCommand> commands;

    public Processor(ProcessorBuilder builder) {
        this.name = builder.getName();
        this.module = builder.getModule();
        this.category = builder.getCategory();
        this.inputProperties = builder.getInputProperties();
        this.outputProperties = builder.getOutputProperties();
        this.commands = builder.getCommands();
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

    public List<ProcessorCommand> getCommands() {
        return commands;
    }

    @Override
    public String toString() {
        return name;
    }

    public void addCommand(ProcessorCommand command) {
        this.commands.add(command);
    }

    public abstract DataBundle execute(DataBundle inputs, DataBundle nodeState) throws CommonException;
    public void preExecute(DataBundle nodeState) throws CommonException {}
    public void postExecute(DataBundle nodeState) throws CommonException {}
    public void onCreated(DataBundle nodeState) throws CommonException {}
    public void onDestroyed(DataBundle nodeState) throws CommonException {}
}
