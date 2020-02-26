package com.visualipcv.core;

public class ProcessorProperty {
    private String name;
    private DataType type;
    private boolean showControl = true;
    private boolean showConnector = true;

    private boolean isFile = false;
    private boolean isColor = false;
    private boolean isRange = false;
    private double step = 0.1;

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

    public ProcessorProperty(String name, DataType type, boolean showConnector, boolean isFile, boolean isColor, boolean isRange, double step) {
        this.name = name;
        this.type = type;
        this.showConnector = showConnector;
        this.isColor = isColor;
        this.isRange = isRange;
        this.isFile = isFile;
        this.step = step;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataType getType() {
        return type;
    }

    public void setType(DataType type) {
        this.type = type;
    }

    public boolean showControl() {
        return showControl;
    }

    public boolean showConnector() {
        return showConnector;
    }

    public boolean isRange() {
        return isRange;
    }

    public boolean isFile() {
        return isFile;
    }

    public boolean isColor() {
        return isColor;
    }

    public double getStep() {
        return step;
    }
}
