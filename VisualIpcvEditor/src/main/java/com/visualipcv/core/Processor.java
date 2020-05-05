package com.visualipcv.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Processor {
    private String name;
    private String module;
    private String category;
    private List<ProcessorProperty> inputProperties;
    private List<ProcessorProperty> outputProperties;
    private List<ProcessorCommand> commands;
    private boolean isProperty = false;

    public Processor(ProcessorBuilder builder) throws CommonException {
        rebuild(builder);
    }

    protected void rebuild(ProcessorBuilder builder) throws CommonException {
        validate(builder);
        this.name = builder.getName();
        this.module = builder.getModule();
        this.category = builder.getCategory();
        this.inputProperties = builder.getInputProperties();
        this.outputProperties = builder.getOutputProperties();
        this.commands = builder.getCommands();
        this.isProperty = builder.getIsProperty();
    }

    public String getName() {
        return name;
    }

    public String getModule() {
        return module;
    }

    public ProcessorUID getUID() {
        return new ProcessorUID(getName(), getModule());
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
        return Collections.unmodifiableList(inputProperties);
    }

    public List<ProcessorProperty> getOutputProperties() {
        return Collections.unmodifiableList(outputProperties);
    }

    public List<ProcessorCommand> getCommands() {
        return commands;
    }

    public boolean isProperty() {
        return isProperty;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return name;
    }

    public void addCommand(ProcessorCommand command) {
        this.commands.add(command);
    }

    public abstract DataBundle execute(DataBundle inputs, DataBundle state) throws CommonException;
    public void preExecute(DataBundle state) throws CommonException {}
    public void postExecute(DataBundle state) throws CommonException {}
    public void onCreate() throws CommonException {}
    public void onDestroy() throws CommonException {}

    private void validate(ProcessorBuilder builder) throws CommonException {
        Set<String> propertyNames = new HashSet<>();

        List<ProcessorProperty> properties = new ArrayList<>(builder.getInputProperties());
        properties.addAll(builder.getOutputProperties());

        for(ProcessorProperty property : properties) {
            if(propertyNames.contains(property.getName())) {
                throw new CommonException(getClass().getName() + ": Names of properties must be unique inside one Processor");
            }

            propertyNames.add(property.getName());
        }
    }
}
