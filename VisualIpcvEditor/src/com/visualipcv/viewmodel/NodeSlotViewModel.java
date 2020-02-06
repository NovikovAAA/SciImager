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

public class NodeSlotViewModel extends ViewModel {
    private NodeSlot nodeSlot;
    private NodeViewModel node;

    private BooleanProperty isConnected = new SimpleBooleanProperty();
    private ObjectProperty<Paint> background = new SimpleObjectProperty<>();
    private ObjectProperty<Paint> stroke = new SimpleObjectProperty<>();

    public NodeSlotViewModel(NodeViewModel node, NodeSlot slot) {
        this.nodeSlot = slot;
        this.node = node;
        update();
    }

    public NodeSlot getNodeSlot() {
        return nodeSlot;
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

    public void connect(NodeSlotViewModel other) {
        nodeSlot.connect(other.getNodeSlot());
        node.getGraph().update();
    }

    public void disconnect() {
        nodeSlot.disconnect();
        node.getGraph().update();
    }

    @Override
    public void update() {
        isConnected.set(nodeSlot.isConnected());

        stroke.set(new Color(
                nodeSlot.getProperty().getType().getColor().getRed() / 255.0,
                nodeSlot.getProperty().getType().getColor().getGreen() / 255.0,
                nodeSlot.getProperty().getType().getColor().getBlue() / 255.0,
                1.0));

        background.set(new Color(
                nodeSlot.getProperty().getType().getColor().getRed() / 500.0,
                nodeSlot.getProperty().getType().getColor().getGreen() / 500.0,
                nodeSlot.getProperty().getType().getColor().getBlue() / 500.0,
                1.0));
    }
}
