package com.visualipcv.view;

import com.visualipcv.core.DataType;
import com.visualipcv.core.InputNodeSlot;
import com.visualipcv.core.NodeSlot;
import com.visualipcv.viewmodel.InputFieldViewModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class InputFieldView extends Pane {
    private InputFieldViewModel viewModel;

    private ObjectProperty<Object> valueProperty = new SimpleObjectProperty<>();
    private static InputFieldFactory factory = new DefaultInputFieldFactory();

    InputFieldView(NodeSlot inputSlot) {
        viewModel = new InputFieldViewModel(inputSlot);

        DataType type = inputSlot.getProperty().getType();
        valueProperty.bindBidirectional(viewModel.getValueProperty());

        Node element = factory.create(this, type);

        if(element != null)
            getChildren().add(element);
    }

    public ObjectProperty<Object> getValueProperty() {
        return valueProperty;
    }

    public static InputFieldFactory getFactory() {
        return factory;
    }

    public void setFactory(InputFieldFactory factory) {
        InputFieldView.factory = factory;
    }
}
