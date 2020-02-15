package com.visualipcv.viewmodel.fields;

import com.visualipcv.core.InputNodeSlot;
import com.visualipcv.viewmodel.ViewModel;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class NumberViewModel extends ViewModel {
    private InputNodeSlot inputNodeSlot;

    private StringProperty value = new SimpleStringProperty();
    private ObjectProperty<Border> border = new SimpleObjectProperty<>();

    public NumberViewModel(InputNodeSlot inputNodeSlot) {
        this.inputNodeSlot = inputNodeSlot;

        value.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    inputNodeSlot.setValue(Double.parseDouble(newValue));
                    border.setValue(null);
                } catch (Exception e) {
                    border.setValue(ErrorBorder.create());
                }
            }
        });

        update();
    }

    public StringProperty getValueProperty() {
        return value;
    }

    public ObjectProperty<Border> getBorderProperty() {
        return border;
    }

    @Override
    public void update() {
        value.set(inputNodeSlot.getValue().toString());
    }
}
