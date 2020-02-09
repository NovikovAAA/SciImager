package com.visualipcv.viewmodel;

import com.visualipcv.core.InputNodeSlot;
import com.visualipcv.core.NodeSlot;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class InputFieldViewModel extends ViewModel {
    private NodeSlot slot;

    private ObjectProperty<Object> valueProperty = new SimpleObjectProperty<>();

    public InputFieldViewModel(NodeSlot slot) {
        this.slot = slot;

        valueProperty.addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
                ((InputNodeSlot)slot).setValue(newValue);
            }
        });

        valueProperty.set(slot.getProperty().getType().getDefaultValue());
    }

    public ObjectProperty<Object> getValueProperty() {
        return valueProperty;
    }

    @Override
    public void update() {

    }
}
