package com.visualipcv.viewmodel.fields;

import com.visualipcv.core.InputNodeSlot;
import com.visualipcv.viewmodel.ViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class StringViewModel extends ViewModel {
    private InputNodeSlot inputNodeSlot;

    private StringProperty value = new SimpleStringProperty();

    public StringViewModel(InputNodeSlot inputNodeSlot) {
        this.inputNodeSlot = inputNodeSlot;

        value.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                inputNodeSlot.setValue(value.get());
            }
        });

        update();
    }

    public StringProperty getValueProperty() {
        return value;
    }

    @Override
    public void update() {
        value.set(inputNodeSlot.getValue().toString());
    }
}
