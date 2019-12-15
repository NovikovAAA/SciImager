package com.visualipcv;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Processor {
    private String name;
    private String category;
    private ArrayList<ProcessorProperty> inputProperties;
    private ArrayList<ProcessorProperty> outputProperties;

    public Processor(String name, String category, ArrayList<ProcessorProperty> inputProperties, ArrayList<ProcessorProperty> outputProperties) {
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

    private ProcessorProperty getProperty(String name, ArrayList<ProcessorProperty> properties) {
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

    public ArrayList<ProcessorProperty> getInputProperties() {
        return inputProperties;
    }

    public ArrayList<ProcessorProperty> getOutputProperties() {
        return outputProperties;
    }

    @Override
    public String toString() {
        return name;
    }
}
