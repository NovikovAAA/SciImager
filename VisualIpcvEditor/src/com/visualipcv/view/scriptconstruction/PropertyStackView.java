package com.visualipcv.view.scriptconstruction;

import com.visualipcv.viewmodel.PropertyViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class PropertyStackView extends VBox {
    private StringProperty titleProperty = new SimpleStringProperty("Properties");
    private PropertyView propertyView1 = new PropertyView();
    private PropertyView propertyView2 = new PropertyView();

    public PropertyStackView(String name) {
        Label title = new Label();
        title.textProperty().bind(titleProperty);
        title.setPadding(new Insets(3.0));
        titleProperty.set(name);

        Button addButton = new Button("Add");
        setMargin(addButton, new Insets(3.0));
        addButton.setPrefWidth(Double.MAX_VALUE);

        propertyView1.setContext(new PropertyViewModel());
        propertyView2.setContext(new PropertyViewModel());

        getChildren().add(title);
        getChildren().add(propertyView1.getView());
        getChildren().add(propertyView2.getView());
        getChildren().add(addButton);
    }

    public StringProperty titleProperty() {
        return titleProperty;
    }

    public void setTitle(String title) {
        titleProperty.set(title);
    }

    public String getTitle() {
        return titleProperty.get();
    }
}
