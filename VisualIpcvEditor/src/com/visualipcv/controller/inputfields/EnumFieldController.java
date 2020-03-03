package com.visualipcv.controller.inputfields;

import com.visualipcv.controller.BorderUtils;
import com.visualipcv.controller.Controller;
import com.visualipcv.controller.InputFieldController;
import com.visualipcv.controller.binding.PropertyChangedEventListener;
import com.visualipcv.controller.binding.UIProperty;
import com.visualipcv.core.DataType;
import com.visualipcv.core.InputNodeSlot;
import com.visualipcv.core.ValidationException;
import com.visualipcv.core.dataconstraints.EnumConstraint;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class EnumFieldController extends Controller<Pane> {
    private ComboBox<Object> valueField;
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
        valueField.setStyle("-fx-padding: " + InputFieldController.STD_TEXT_PADDING + "; -fx-label-padding: " + InputFieldController.STD_TEXT_PADDING);
        valueField.getItems().addAll(constraint.getValues());
        valueField.setValue(valueField.getItems().get(0));

        valueProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                for(Object value : valueField.getItems()) {
                    if(((EnumConstraint.Value)value).getValue() == newValue)
                        valueField.setValue(value);
                }
            }
        });

        valueProperty.setBinder((Object slot) -> {
            return ((InputNodeSlot)slot).getValue();
        });

        valueField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    ((InputNodeSlot)getContext()).setValue(((EnumConstraint.Value)valueField.getValue()).getValue());
                    valueField.setBorder(null);
                    poll(valueProperty);
                } catch (ValidationException e) {
                    valueField.setBorder(BorderUtils.createErrorBorder());
                }
            }
        });

        getView().getChildren().add(valueField);
    }
}
