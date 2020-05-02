package com.visualipcv.utils;

import com.visualipcv.core.DataType;
import com.visualipcv.core.DataTypes;
import com.visualipcv.core.Node;
import com.visualipcv.core.NodeSlot;

public class ProcUtils {
    public static void shareType(Node node) {
        DataType targetType = null;

        for(NodeSlot slot : node.getInputSlots()) {
            if(slot.getProperty().getType() == DataTypes.ANY && slot.getConnectedType() != null) {
                targetType = slot.getConnectedType();
                break;
            }
        }

        if(targetType == null) {
            for(NodeSlot slot : node.getOutputSlots()) {
                if(slot.getProperty().getType() == DataTypes.ANY && slot.getConnectedType() != null) {
                    targetType = slot.getConnectedType();
                    break;
                }
            }
        }

        for(NodeSlot slot : node.getInputSlots())
            if(slot.getProperty().getType() == DataTypes.ANY)
                slot.overrideType(targetType);
        for(NodeSlot slot : node.getOutputSlots())
            if(slot.getProperty().getType() == DataTypes.ANY)
                slot.overrideType(targetType);
    }
}
