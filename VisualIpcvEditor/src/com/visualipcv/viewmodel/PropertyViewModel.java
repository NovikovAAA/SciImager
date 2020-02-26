package com.visualipcv.viewmodel;

import com.visualipcv.core.DataType;
import com.visualipcv.core.ProcessorProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class PropertyViewModel extends ViewModel {
    private ProcessorProperty property;

    private ObjectProperty<DataType> dataType = new SimpleObjectProperty<>();
    private StringProperty name = new SimpleStringProperty();

    public PropertyViewModel() {
        property = new ProcessorProperty("New property", DataType.NUMBER);

        dataType.addListener(new ChangeListener<DataType>() {
            @Override
            public void changed(ObservableValue<? extends DataType> observable, DataType oldValue, DataType newValue) {
                property.setType(newValue);
            }
        });

        name.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                property.setName(newValue);
            }
        });

        update();
    }

    @Override
    public void update() {
        name.set(property.getName());
        dataType.set(property.getType());
    }

    public ProcessorProperty getModel() {
        return property;
    }
}
