package com.visualipcv.controller.scriptconstruction;

import com.visualipcv.core.DataType;
import com.visualipcv.core.DataTypeLibrary;
import com.visualipcv.core.ProcessorProperty;
import com.visualipcv.controller.Controller;
import com.visualipcv.controller.binding.PropertyChangedEventListener;
import com.visualipcv.controller.binding.UIProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class PropertyController extends Controller<AnchorPane> {
    @FXML
    private Button moveUpButton;
    @FXML
    private Button moveDownButton;
    @FXML
    private ComboBox<DataType> dataType;
    @FXML
    private TextField name;

    private UIProperty nameProperty = new UIProperty();
    private UIProperty dataTypeProperty = new UIProperty();

    public PropertyController() {
        super(AnchorPane.class, "PropertyView.fxml");

        nameProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                name.setText((String)newValue);
            }
        });

        dataTypeProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                dataType.setValue((DataType)newValue);
            }
        });

        nameProperty.setBinder((Object property) -> {
            return ((ProcessorProperty)property).getName();
        });

        dataTypeProperty.setBinder((Object property) -> {
            return ((ProcessorProperty)property).getType();
        });

        name.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue && oldValue) {
                    PropertyController.this.<ProcessorProperty>getContext().setName(name.getText());
                }
            }
        });

        name.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PropertyController.this.<ProcessorProperty>getContext().setName(name.getText());
            }
        });

        dataType.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PropertyController.this.<ProcessorProperty>getContext().setType(dataType.getValue());
            }
        });

        dataType.getItems().addAll(DataTypeLibrary.getDataTypes());
        initialize();
    }
}
