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

public class VectorNFieldController extends Controller<Pane> {
    private TextField[] textFields;
    private UIProperty valueProperty = new UIProperty();

    public VectorNFieldController(int n) {
        super(Pane.class);

        textFields = new TextField[n];

        for(int i = 0; i < n; i++) {
            textFields[i] = new TextField();
            textFields[i].setPrefSize(
                    (InputFieldController.STD_WIDTH - InputFieldController.STD_MARGIN * (n - 1)) / n,
                    InputFieldController.STD_HEIGHT);
            textFields[i].setMaxSize(textFields[i].getPrefWidth(), textFields[i].getPrefHeight());
            textFields[i].setMinSize(textFields[i].getPrefWidth(), textFields[i].getPrefHeight());
            textFields[i].setLayoutX((textFields[i].getPrefWidth() + InputFieldController.STD_MARGIN) * i);
            textFields[i].setFont(new Font(InputFieldController.STD_FONT_SIZE));
            textFields[i].setPadding(new Insets(InputFieldController.STD_TEXT_PADDING));

            textFields[i].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Double[] values = new Double[n];

                    for(int i = 0; i < n; i++) {
                        try {
                            values[i] = Double.parseDouble(textFields[i].getText());
                            textFields[i].setBorder(null);
                        } catch (Exception e) {
                            textFields[i].setBorder(BorderUtils.createErrorBorder());
                        }
                    }

                    ((InputNodeSlot)getContext()).setValue(values);
                    poll(valueProperty);
                }
            });

            textFields[i].focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if(!newValue) {
                        Double[] values = new Double[n];

                        for(int i = 0; i < n; i++) {
                            try {
                                values[i] = Double.parseDouble(textFields[i].getText());
                                textFields[i].setBorder(null);
                            } catch (Exception e) {
                                textFields[i].setBorder(BorderUtils.createErrorBorder());
                            }
                        }

                        ((InputNodeSlot)getContext()).setValue(values);
                        poll(valueProperty);
                    }
                }
            });
        }

        getView().getChildren().addAll(textFields);

        valueProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                Double[] values = (Double[])newValue;

                if(values == null)
                    return;

                for(int i = 0; i < n ; i++) {
                    if(values[i] == null)
                        continue;

                    textFields[i].setText(values[i].toString());
                }
            }
        });

        valueProperty.setBinder((Object nodeSlot) -> {
            InputNodeSlot slot = (InputNodeSlot)nodeSlot;
            return slot.getValue();
        });

        initialize();
    }
}
