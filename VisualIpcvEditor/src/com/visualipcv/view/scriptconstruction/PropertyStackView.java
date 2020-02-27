package com.visualipcv.view.scriptconstruction;

import com.visualipcv.view.ViewBase;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class PropertyStackView extends ViewBase<AnchorPane> {
    @FXML
    private Label title;
    @FXML
    private VBox propertyList;
    @FXML
    private Button addButton;

    private StringProperty titleProperty = new SimpleStringProperty("Properties");

    public PropertyStackView(String name) {
        super(AnchorPane.class, "PropertyStackView.fxml");

        title.textProperty().bindBidirectional(titleProperty);
        titleProperty.set(name);

        /*Button addButton = new Button("Add");
        VBox.setMargin(addButton, new Insets(3.0));
        addButton.setPrefWidth(Double.MAX_VALUE);

        propertyView1.setContext(new PropertyViewModel());
        propertyView2.setContext(new PropertyViewModel());

        getView().getChildren().add(title);
        getView().getChildren().add(propertyView1.getView());
        getView().getChildren().add(propertyView2.getView());
        getView().getChildren().add(addButton);*/
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
