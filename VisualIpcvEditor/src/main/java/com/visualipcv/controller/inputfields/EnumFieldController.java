package com.visualipcv.controller.inputfields;

import com.visualipcv.controller.BorderUtils;
import com.visualipcv.controller.Controller;
import com.visualipcv.controller.InputFieldController;
import com.visualipcv.controller.binding.PropertyChangedEventListener;
import com.visualipcv.controller.binding.UIProperty;
import com.visualipcv.core.DataType;
import com.visualipcv.core.InputNodeSlot;
import com.visualipcv.core.NodeSlot;
import com.visualipcv.core.ValidationException;
import com.visualipcv.core.dataconstraints.EnumConstraint;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

import javax.swing.*;

public class EnumFieldController extends Controller<Pane> {
    private ComboBox<EnumConstraint.Value> valueField;
    private UIProperty valueProperty = new UIProperty();

    public EnumFieldController(DataType type) {
        super(Pane.class);
        EnumConstraint constraint = type.getConstraint(EnumConstraint.class);

        if(constraint == null)
            return;

        valueField = new ComboBox<>();
        valueField.setPrefSize(InputFieldController.STD_WIDTH, InputFieldController.STD_HEIGHT);
        valueField.setMinSize(valueField.getPrefWidth(), valueField.getPrefHeight());
        valueField.setMaxSize(valueField.getPrefWidth(), valueField.getPrefHeight());
        valueField.setPadding(new Insets(InputFieldController.STD_TEXT_PADDING));
        valueField.getItems().addAll(constraint.getValues());
        valueField.setEditable(true);
        valueField.setValue(valueField.getItems().get(0));

        valueProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                for(Object value : valueField.getItems()) {
                    if(((EnumConstraint.Value)value).getValue().equals(newValue))
                        valueField.setValue((EnumConstraint.Value)value);
                }
            }
        });

        valueProperty.setBinder((Object slot) -> {
            return InputFieldController.getValueFromSlot((NodeSlot)slot);
        });

        valueField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onAction();
            }
        });

        valueField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                onAction();
            }
        });

        getView().getChildren().add(valueField);
    }

    public void onAction() {
        try {
            Object value = valueField.getValue();

            if(value instanceof EnumConstraint.Value) {
                ((InputNodeSlot)getContext()).setValue(((EnumConstraint.Value)value).getValue());
                valueField.setBorder(null);
                poll(valueProperty);
            } else if(value instanceof String) {
                for(EnumConstraint.Value v : valueField.getItems()) {
                    if(v.getVisual().equals(valueField.getEditor().getText())) {
                        ((InputNodeSlot)getContext()).setValue(v.getValue());
                        valueField.setValue(v);
                        poll(valueProperty);
                        return;
                    }
                }
                valueField.setBorder(BorderUtils.createErrorBorder());
            }
        } catch (ValidationException e) {
            valueField.setBorder(BorderUtils.createErrorBorder());
        }
    }
}
