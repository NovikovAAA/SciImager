package com.visualipcv.core.io;

import com.visualipcv.core.Connection;

import java.io.Serializable;
import java.util.UUID;

public class ConnectionEntity implements Serializable {
    private UUID sourceNodeId;
    private UUID targetNodeId;
    private String sourceNodeProperty;
    private String targetNodeProperty;

    public ConnectionEntity(Connection connection) {
        sourceNodeId = connection.getSource().getNode().getId();
        targetNodeId = connection.getTarget().getNode().getId();
        sourceNodeProperty = connection.getSource().getProperty().getName();
        targetNodeProperty = connection.getTarget().getProperty().getName();
    }

    public UUID getSourceNodeId() {
        return sourceNodeId;
    }

    public UUID getTargetNodeId() {
        return targetNodeId;
    }

    public String getSourceNodeProperty() {
        return sourceNodeProperty;
    }

    public String getTargetNodeProperty() {
        return targetNodeProperty;
    }
}
