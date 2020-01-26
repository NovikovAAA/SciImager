package com.visualipcv;

import com.visualipcv.view.NodeSlotType;
import com.visualipcv.view.events.NodeSlotEventListener;
import sun.util.resources.cldr.ar.CalendarData_ar_YE;

public class InputNodeSlot extends NodeSlot {
    private Object value;
    private OutputNodeSlot input;

    public InputNodeSlot(Node node, ProcessorProperty property) {
        super(node, property);
        value = createDefaultValue();
    }

    public Object createDefaultValue() {
        if(getProperty().getType() == DataType.NUMBER) {
            return 0.0f;
        } else if(getProperty().getType() == DataType.VECTOR2) {
            return new Float[] { 0.0f, 0.0f };
        } else if(getProperty().getType() == DataType.VECTOR3) {
            return new Float[] { 0.0f, 0.0f, 0.0f };
        } else if(getProperty().getType() == DataType.VECTOR4) {
            return new Float[] { 0.0f, 0.0f, 0.0f, 0.0f };
        }
        return null;
    }

    public void connect(NodeSlot slot) {
        if(!(slot instanceof OutputNodeSlot)) {
            throw new IllegalArgumentException("Cannot connect input slot to input slot");
        }

        if(input != null) {
            throw new IllegalArgumentException("Input slot can be connected to only one other slot");
        }

        if(slot.getProperty().getType() != getProperty().getType()) {
            throw new IllegalArgumentException("Slot types mismatch");
        }

        this.input = (OutputNodeSlot)slot;
        onConnected(slot);
    }

    public void disconnect() {
        onDisconnected(input);
        this.input = null;
    }

    public OutputNodeSlot getConnectedSlot() {
        return input;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}
