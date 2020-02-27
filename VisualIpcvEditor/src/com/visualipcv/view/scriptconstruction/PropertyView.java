package com.visualipcv.view.scriptconstruction;

import com.visualipcv.core.DataType;
import com.visualipcv.core.DataTypeLibrary;
import com.visualipcv.view.Binding;
import com.visualipcv.view.BindingType;
import com.visualipcv.view.ViewBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class PropertyView extends ViewBase<AnchorPane> {
    @FXML
    private Button moveUpButton;
    @FXML
    private Button moveDownButton;
    @FXML
    private ComboBox<DataType> dataType;
    @FXML
    private TextField name;

    @Binding(value = "dataType", type = BindingType.ToSource)
    private ObjectProperty<DataType> dataTypeProperty = new SimpleObjectProperty<>(DataType.NUMBER);

    @Binding(value = "name", type = BindingType.ToSource)
    private StringProperty nameProperty = new SimpleStringProperty("New property");

    public PropertyView() {
        super(AnchorPane.class, "PropertyView.fxml");

        VBox.setMargin(getView(), new Insets(3.0));

        dataTypeProperty.bindBidirectional(dataType.valueProperty());
        nameProperty.bindBidirectional(name.textProperty());
        dataType.getItems().addAll(DataTypeLibrary.getDataTypes());
    }

    public ObjectProperty<DataType> dataTypeProperty() {
        return dataTypeProperty;
    }

    public void setDataType(DataType type) {
        dataTypeProperty.set(type);
    }

    public DataType getDataType() {
        return dataTypeProperty.get();
    }

    public StringProperty nameProperty() {
        return nameProperty;
    }

    public void setName(String name) {
        nameProperty.set(name);
    }

    public String getName() {
        return nameProperty.get();
    }
}
