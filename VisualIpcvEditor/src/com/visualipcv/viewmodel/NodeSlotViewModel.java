package com.visualipcv.viewmodel;

import com.visualipcv.core.NodeSlot;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class NodeSlotViewModel {
    private ObjectProperty<NodeSlot> nodeSlot = new SimpleObjectProperty<>();

    public NodeSlotViewModel() {

    }

    public ObjectProperty<NodeSlot> getNodeSlot() {
        return nodeSlot;
    }

    public ObjectProperty<NodeSlot> nodeSlotProperty() {
        return nodeSlot;
    }
}
