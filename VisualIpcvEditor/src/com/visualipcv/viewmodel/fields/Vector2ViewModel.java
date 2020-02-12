package com.visualipcv.viewmodel.fields;

import com.visualipcv.core.InputNodeSlot;
import com.visualipcv.viewmodel.ViewModel;
import javafx.beans.property.ObjectProperty;
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

public class Vector2ViewModel extends ViewModel {
    private InputNodeSlot inputNodeSlot;

    private StringProperty xValue = new SimpleStringProperty();
    private StringProperty yValue = new SimpleStringProperty();

    private ObjectProperty<Border> xBorder = new SimpleObjectProperty<>();
    private ObjectProperty<Border> yBorder = new SimpleObjectProperty<>();

    public Vector2ViewModel(InputNodeSlot inputNodeSlot) {
        this.inputNodeSlot = inputNodeSlot;

        xValue.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Double[] value = (Double[])inputNodeSlot.getValue();

                try {
                    value[0] = Double.parseDouble(newValue);
                    inputNodeSlot.setValue(value);
                    xBorder.set(null);
                } catch (Exception e) {
                    xBorder.set(ErrorBorder.create());
                }
            }
        });

        yValue.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Double[] value = (Double[])inputNodeSlot.getValue();

                try {
                    value[1] = Double.parseDouble(newValue);
                    inputNodeSlot.setValue(value);
                    yBorder.set(null);
                } catch (Exception e) {
                    yBorder.set(ErrorBorder.create());
                }
            }
        });

        update();
    }

    public StringProperty getXValueProperty() {
        return xValue;
    }

    public StringProperty getYValueProperty() {
        return yValue;
    }

    public ObjectProperty<Border> getXBorderProperty() {
        return xBorder;
    }

    public ObjectProperty<Border> getYBorderProperty() {
        return yBorder;
    }

    @Override
    public void update() {
        Double[] value = (Double[])inputNodeSlot.getValue();
        xValue.set(value[0].toString());
        yValue.set(value[1].toString());
    }
}
