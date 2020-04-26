package com.visualipcv.core.io;

import com.visualipcv.core.DataType;
import com.visualipcv.core.ProcessorProperty;

import java.io.Serializable;

public class ProcessorPropertyEntity implements Serializable {
    private String name;
    private String type;
    private boolean showControl = true;
    private boolean showConnector = true;

    public ProcessorPropertyEntity(ProcessorProperty property) {
        this.name = property.getName();
        this.type = property.getType().getName();
        this.showControl = property.showControl();
        this.showConnector = property.showConnector();
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean showControl() {
        return showControl;
    }

    public boolean showConnector() {
        return showConnector;
    }
}
