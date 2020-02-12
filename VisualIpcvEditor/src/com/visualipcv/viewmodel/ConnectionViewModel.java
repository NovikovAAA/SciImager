package com.visualipcv.viewmodel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Paint;

public class ConnectionViewModel extends ViewModel {
    private NodeSlotViewModel source;
    private NodeSlotViewModel target;

    private ObjectProperty<Paint> color = new SimpleObjectProperty<>();

    public ConnectionViewModel(NodeSlotViewModel source, NodeSlotViewModel target) {
        this.source = source;
        this.target = target;
        update();
    }

    @Override
    public void update() {
        color.bind(source.getStrokeProperty());
    }

    public ObjectProperty<Paint> getColorProperty() {
        return color;
    }

    public NodeSlotViewModel getSource() {
        return source;
    }

    public NodeSlotViewModel getTarget() {
        return target;
    }
}
