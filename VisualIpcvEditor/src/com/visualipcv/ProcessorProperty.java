package com.visualipcv;

import com.visualipcv.view.NodeSlotType;

public class ProcessorProperty {
    private String name;
    private DataType type;

    public ProcessorProperty(String name, DataType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public DataType getType() {
        return type;
    }
}
