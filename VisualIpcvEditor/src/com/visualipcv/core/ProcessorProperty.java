package com.visualipcv.core;

public class ProcessorProperty {
    private String name;
    private DataType type;
    private boolean showControl = true;
    private boolean showConnector = true;

    public ProcessorProperty(String name, DataType type) {
        this.name = name;
        this.type = type;
    }

    public ProcessorProperty(String name, DataType type, boolean showControl, boolean showConnector) {
        this.name = name;
        this.type = type;
        this.showControl = showControl;
        this.showConnector = showConnector;
    }

    public String getName() {
        return name;
    }

    public DataType getType() {
        return type;
    }

    public boolean showControl() {
        return showControl;
    }

    public boolean showConnector() {
        return showConnector;
    }
}
