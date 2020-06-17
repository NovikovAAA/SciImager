package com.visualipcv.core;

public class ProcessorPropertyBuilder {
    private String name;
    private DataType type;
    private boolean showControl = false;
    private boolean showConnector = false;
    private boolean isArray = false;

    public ProcessorPropertyBuilder(String name, DataType type) {
        this.name = name;
        this.type = type;
    }

    public ProcessorPropertyBuilder addControl() {
        showControl = true;
        return this;
    }

    public ProcessorPropertyBuilder addConnector() {
        showConnector = true;
        return this;
    }

    public ProcessorPropertyBuilder makeArray() {
        isArray = true;
        return this;
    }

    public String getName() {
        return name;
    }

    public DataType getType() {
        return type;
    }

    public boolean isShowControl() {
        return showControl;
    }

    public boolean isShowConnector() {
        return showConnector;
    }

    public boolean isArray() {
        return isArray;
    }
}
