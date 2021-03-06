package com.visualipcv.core;

import com.visualipcv.core.io.NodeSlotEntity;

import java.util.ArrayList;
import java.util.Properties;

public abstract class NodeSlot {
    private ProcessorProperty property;
    private Node node;
    private DataType typeOverride;

    public NodeSlot(Node node, ProcessorProperty property) {
        this.property = property;
        this.node = node;
    }

    public NodeSlot(Node node, NodeSlotEntity entity) {
        this.node = node;
        property = new ProcessorProperty(
                entity.getName(),
                DataTypeLibrary.getByName(entity.getDataTypeName()),
                entity.showControl(),
                entity.showConnector());
    }

    public ProcessorProperty getProperty() {
        return property;
    }

    void setProperty(ProcessorProperty property) {
        this.property = property;
    }

    public Node getNode() {
        return node;
    }

    public DataType getActualType() {
        if(getProperty().getType() != DataTypes.ANY)
            return getProperty().getType();

        if(getTypeOverride() != null)
            return getTypeOverride();

        DataType type = null;

        for(Connection connection : getNode().getGraph().getConnections(this)) {
            if(connection.getTarget() == this) {
                DataType conType = connection.getSource().getTypeOverride();
                return conType == null ? connection.getSource().getProperty().getType() : conType;
            }
            else if(connection.getSource() == null) {
                DataType conType = connection.getTarget().getTypeOverride();
                return conType == null ? connection.getTarget().getProperty().getType() : conType;
            }
        }

        return getProperty().getType();
    }

    public DataType getTypeOverride() {
        return typeOverride;
    }

    public void overrideType(DataType type) {
        if(getProperty().getType() != DataTypes.ANY)
            throw new RuntimeException("Cannot override concrete type");

        this.typeOverride = type;
    }

    public DataType getConnectedType() {
        for(Connection connection : getNode().getGraph().getConnections(this)) {
            if(connection.getTarget() == this && connection.getSource().getActualType() != DataTypes.ANY)
                return connection.getSource().getActualType();
            if(connection.getSource() == this && connection.getTarget().getActualType() != DataTypes.ANY)
                return connection.getTarget().getActualType();
        }
        return null;
    }

    public static boolean isConnectionAvailable(NodeSlot slot1, NodeSlot slot2) {
        if(slot1.getNode().isProxy() || slot2.getNode().isProxy()) {
            return false;
        }

        if(slot1.getClass() == slot2.getClass())
            return false;

        if(slot1.getNode().getGraph() != slot2.getNode().getGraph())
            return false;

        if(slot1.getProperty().isArray() != slot2.getProperty().isArray())
            return false;

        DataType originType1 = slot1.getProperty().getType();
        DataType originType2 = slot2.getProperty().getType();

        DataType overridden1 = slot1.getTypeOverride();
        DataType overridden2 = slot2.getTypeOverride();

        DataType realType1 = overridden1 == null ? originType1 : overridden1;
        DataType realType2 = overridden2 == null ? originType2 : overridden2;

        if(realType1 == realType2 || realType1 == DataTypes.ANY || realType2 == DataTypes.ANY)
            return true;

        if(slot1 instanceof OutputNodeSlot) {
            return Converter.isConvertible(realType1, realType2);
        } else {
            return Converter.isConvertible(realType2, realType1);
        }
    }

    public abstract void connect(NodeSlot other);
    public abstract void disconnect();
    public abstract boolean isConnected();
}
