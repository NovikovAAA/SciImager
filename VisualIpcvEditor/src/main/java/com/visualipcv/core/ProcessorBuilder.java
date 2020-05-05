package com.visualipcv.core;

import java.util.ArrayList;
import java.util.List;

public class ProcessorBuilder {
    private String name;
    private String module;
    private String category;
    private List<ProcessorProperty> inputProperties = new ArrayList<>();
    private List<ProcessorProperty> outputProperties = new ArrayList<>();
    private List<ProcessorCommand> commands = new ArrayList<>();
    private boolean isProperty = false;

    public ProcessorBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ProcessorBuilder setModule(String module) {
        this.module = module;
        return this;
    }

    public ProcessorBuilder setCategory(String category) {
        this.category = category;
        return this;
    }

    public ProcessorBuilder setIsProperty(boolean isProperty) {
        this.isProperty = isProperty;
        return this;
    }

    public ProcessorBuilder addInputProperty(ProcessorProperty property) {
        inputProperties.add(property);
        return this;
    }

    public ProcessorBuilder addOutputProperty(ProcessorProperty property) {
        outputProperties.add(property);
        return this;
    }

    public ProcessorBuilder setInputProperties(List<ProcessorProperty> properties) {
        inputProperties = properties;
        return this;
    }

    public ProcessorBuilder setOutputProperties(List<ProcessorProperty> properties) {
        outputProperties = properties;
        return this;
    }

    public ProcessorBuilder addCommand(ProcessorCommand command) {
        commands.add(command);
        return this;
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

    public List<ProcessorProperty> getInputProperties() {
        return inputProperties;
    }

    public List<ProcessorProperty> getOutputProperties() {
        return outputProperties;
    }

    public List<ProcessorCommand> getCommands() {
        return commands;
    }

    public boolean getIsProperty() {
        return isProperty;
    }
}
