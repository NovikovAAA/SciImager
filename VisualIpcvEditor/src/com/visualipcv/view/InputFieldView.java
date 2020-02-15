package com.visualipcv.view;

import com.visualipcv.core.DataType;
import com.visualipcv.core.InputNodeSlot;
import com.visualipcv.core.NodeSlot;
import com.visualipcv.viewmodel.ViewModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class InputFieldView extends Pane {
    private ViewModel viewModel;

    private static InputFieldFactory factory = new DefaultInputFieldFactory();

    InputFieldView(NodeSlot inputSlot) {
        DataType type = inputSlot.getProperty().getType();
        Node element = factory.create(this, (InputNodeSlot)inputSlot);

        if(element != null)
            getChildren().add(element);
    }

    public void initViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public ViewModel getViewModel() {
        return viewModel;
    }

    public static InputFieldFactory getFactory() {
        return factory;
    }

    public static void setFactory(InputFieldFactory factory) {
        InputFieldView.factory = factory;
    }
}
