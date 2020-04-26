package com.visualipcv.core;

import com.visualipcv.core.io.ProcessorPropertyEntity;

public class ProcessorProperty {
    private String name;
    private DataType type;
    private boolean showControl = true;
    private boolean showConnector = true;

    public ProcessorProperty(ProcessorPropertyEntity entity) {
        this.name = entity.getName();
        this.type = DataTypeLibrary.getByName(entity.getName());
        this.showControl = entity.showControl();
        this.showConnector = entity.showConnector();
    }

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

    public ProcessorProperty(String name, DataType type, boolean showConnector) {
        this.name = name;
        this.type = type;
        this.showConnector = showConnector;
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
}
