package com.visualipcv.controller.inputfields;

import com.visualipcv.controller.Controller;
import com.visualipcv.controller.BorderUtils;
import com.visualipcv.controller.binding.PropertyChangedEventListener;
import com.visualipcv.controller.binding.UIProperty;
import com.visualipcv.core.InputNodeSlot;
import com.visualipcv.controller.InputFieldController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class NumberFieldController extends Controller<Pane> {
    private TextField valueField;
    private UIProperty valueProperty = new UIProperty();

    public NumberFieldController() {
        super(Pane.class);

        valueField = new TextField();
        valueField.setPrefSize(InputFieldController.STD_WIDTH, InputFieldController.STD_HEIGHT);
        valueField.setMaxSize(valueField.getPrefWidth(), valueField.getPrefHeight());
        valueField.setMinSize(valueField.getPrefWidth(), valueField.getPrefHeight());
        valueField.setFont(new Font(InputFieldController.STD_FONT_SIZE));
        valueField.setPadding(new Insets(InputFieldController.STD_TEXT_PADDING));
        getView().getChildren().add(valueField);

        valueProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                valueField.setText((String)newValue);
            }
        });

        valueProperty.setBinder((Object nodeSlot) -> {
            return ((InputNodeSlot)nodeSlot).getValue().toString();
        });

        valueField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    ((InputNodeSlot)getContext()).setValue(Double.parseDouble(valueField.getText()));
                    valueField.setBorder(null);
                    poll(valueProperty);
                } catch (Exception e) {
                    valueField.setBorder(BorderUtils.createErrorBorder());
                }
            }
        });

        valueField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue) {
                    try {
                        ((InputNodeSlot)getContext()).setValue(Double.parseDouble(valueField.getText()));
                        valueField.setBorder(null);
                        poll(valueProperty);
                    } catch (Exception e) {
                        valueField.setBorder(BorderUtils.createErrorBorder());
                    }
                }
            }
        });

        initialize();
    }
}
