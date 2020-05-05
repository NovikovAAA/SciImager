package com.visualipcv.core.io;

import com.visualipcv.core.DataTypeLibrary;
import com.visualipcv.core.NodeSlot;

import java.io.Serializable;

public class NodeSlotEntity implements Serializable {
    private String name;
    private String dataTypeName;
    private boolean showControl;
    private boolean showConnector;

    public NodeSlotEntity(NodeSlot slot) {
        this.name = slot.getProperty().getName();
        this.dataTypeName = slot.getProperty().getType().getName();
        this.showControl = slot.getProperty().showControl();
        this.showConnector = slot.getProperty().showConnector();
    }

    public String getName() {
        return name;
    }

    public String getDataTypeName() {
        return dataTypeName;
    }

    public boolean showControl() {
        return showControl;
    }

    public boolean showConnector() {
        return showConnector;
    }
}
