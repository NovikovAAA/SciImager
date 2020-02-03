package com.visualipcv.viewmodel;

import com.visualipcv.core.NodeSlot;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class NodeSlotViewModel {
    private NodeSlot nodeSlot;

    private BooleanProperty isConnected = new SimpleBooleanProperty();
    private ObjectProperty<Paint> background = new SimpleObjectProperty<>();
    private ObjectProperty<Paint> stroke = new SimpleObjectProperty<>();

    public NodeSlotViewModel(NodeSlot slot) {
        this.nodeSlot = slot;
        isConnected.setValue(false);
        stroke.set(new Color(
                slot.getProperty().getType().getColor().getRed() / 255.0,
                slot.getProperty().getType().getColor().getGreen() / 255.0,
                slot.getProperty().getType().getColor().getBlue() / 255.0,
                1.0));
        background.set(new Color(
                slot.getProperty().getType().getColor().getRed() / 500.0,
                slot.getProperty().getType().getColor().getGreen() / 500.0,
                slot.getProperty().getType().getColor().getBlue() / 500.0,
                1.0));
    }

    public BooleanProperty getIsConnectedProperty() {
        return isConnected;
    }

    public ObjectProperty<Paint> getBackgroundProperty() {
        return background;
    }

    public ObjectProperty<Paint> getStrokeProperty() {
        return stroke;
    }
}
