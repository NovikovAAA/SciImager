package com.visualipcv.controller.scriptconstruction;

import com.visualipcv.core.DataType;
import com.visualipcv.core.ProcessorProperty;
import com.visualipcv.controller.Controller;
import com.visualipcv.controller.binding.PropertyChangedEventListener;
import com.visualipcv.controller.binding.UIProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class PropertyStackController extends Controller<AnchorPane> {
    @FXML
    private Label title;
    @FXML
    private VBox propertyList;
    @FXML
    private Button addButton;

    private UIProperty properties = new UIProperty();

    public PropertyStackController(String name) {
        super(AnchorPane.class, "PropertyStackView.fxml");

        title.textProperty().set(name);

        properties.setBinder((Object context) -> {
            List<PropertyController> views = new ArrayList<>();
            for(ProcessorProperty property : (List<ProcessorProperty>)context) {
                PropertyController propertyView = new PropertyController();
                propertyView.setContext(property);
                views.add(propertyView);
            }
            return views;
        });

        properties.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                List<PropertyController> views = (List<PropertyController>)newValue;
                propertyList.getChildren().clear();

                for(PropertyController view : views) {
                    propertyList.getChildren().add(view.getView());
                }
            }
        });

        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                List<ProcessorProperty> processorProperties = (List<ProcessorProperty>)getContext();
                processorProperties.add(new ProcessorProperty("New Property", DataType.NUMBER));
                poll(properties);
            }
        });

        initialize();
    }
}
