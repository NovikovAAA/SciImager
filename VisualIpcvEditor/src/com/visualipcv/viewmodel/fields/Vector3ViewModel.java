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

public class Vector3ViewModel extends ViewModel {
    private InputNodeSlot inputNodeSlot;

    private StringProperty xValue = new SimpleStringProperty();
    private StringProperty yValue = new SimpleStringProperty();
    private StringProperty zValue = new SimpleStringProperty();

    private ObjectProperty<Border> xBorder = new SimpleObjectProperty<>();
    private ObjectProperty<Border> yBorder = new SimpleObjectProperty<>();
    private ObjectProperty<Border> zBorder = new SimpleObjectProperty<>();

    public Vector3ViewModel(InputNodeSlot inputNodeSlot) {
        this.inputNodeSlot = inputNodeSlot;

        xValue.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Double[] value = (Double[])inputNodeSlot.getValue();

                try {
                    value[0] = Double.parseDouble(xValue.get());
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
                    value[1] = Double.parseDouble(yValue.get());
                    inputNodeSlot.setValue(value);
                    yBorder.set(null);
                } catch (Exception e) {
                    yBorder.set(ErrorBorder.create());
                }
            }
        });

        zValue.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Double[] value = (Double[])inputNodeSlot.getValue();

                try {
                    value[2] = Double.parseDouble(zValue.get());
                    inputNodeSlot.setValue(value);
                    zBorder.set(null);
                } catch (Exception e) {
                    zBorder.set(ErrorBorder.create());
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

    public StringProperty getZValueProperty() {
        return zValue;
    }

    public ObjectProperty<Border> getXBorderProperty() {
        return xBorder;
    }

    public ObjectProperty<Border> getYBorderProperty() {
        return yBorder;
    }

    public ObjectProperty<Border> getZBorderProperty() {
        return zBorder;
    }

    @Override
    public void update() {
        Double[] value = (Double[])inputNodeSlot.getValue();
        xValue.set(value[0].toString());
        yValue.set(value[1].toString());
        zValue.set(value[2].toString());
    }
}