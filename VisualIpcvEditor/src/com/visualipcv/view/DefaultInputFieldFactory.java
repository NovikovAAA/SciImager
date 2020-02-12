package com.visualipcv.view;

import com.visualipcv.core.DataType;
import com.visualipcv.core.InputNodeSlot;
import com.visualipcv.core.NodeSlot;
import com.visualipcv.utils.UIHighlight;
import com.visualipcv.viewmodel.ViewModel;
import com.visualipcv.viewmodel.fields.NumberViewModel;
import com.visualipcv.viewmodel.fields.StringViewModel;
import com.visualipcv.viewmodel.fields.Vector2ViewModel;
import com.visualipcv.viewmodel.fields.Vector3ViewModel;
import com.visualipcv.viewmodel.fields.Vector4ViewModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class DefaultInputFieldFactory extends InputFieldFactory {
    private static double STD_WIDTH = 180.0;
    private static double STD_HEIGHT = 25.0;
    private static double STD_MARGIN = 10.0;
    private static double STD_TEXT_PADDING = 3.0;

    private void setSize(Region control, double x, double y) {
        control.setPrefWidth(x);
        control.setPrefHeight(y);
        control.setMaxWidth(x);
        control.setMaxHeight(y);
        control.setMinWidth(x);
        control.setMinHeight(y);
    }

    private void setFont(TextInputControl control) {
        control.setFont(new Font(14.0));
    }

    private void setPadding(TextInputControl control) {
        control.setPadding(new Insets(STD_TEXT_PADDING));
    }

    @Override
    public Node create(InputFieldView fieldView, InputNodeSlot slot) {
        if(slot.getProperty().getType() == DataType.NUMBER) {
            NumberViewModel viewModel = new NumberViewModel(slot);
            TextField field = new TextField();
            setSize(field, STD_WIDTH, STD_HEIGHT);
            setFont(field);
            setPadding(field);
            field.textProperty().bindBidirectional(viewModel.getValueProperty());
            field.borderProperty().bind(viewModel.getBorderProperty());
            fieldView.initViewModel(viewModel);
            return field;
        } else if(slot.getProperty().getType() == DataType.STRING) {
            StringViewModel viewModel = new StringViewModel(slot);
            TextField field = new TextField();
            setSize(field, STD_WIDTH, STD_HEIGHT);
            setFont(field);
            setPadding(field);
            field.textProperty().bindBidirectional(viewModel.getValueProperty());
            fieldView.initViewModel(viewModel);
            return field;
        } else if(slot.getProperty().getType() == DataType.VECTOR2) {
            Vector2ViewModel viewModel = new Vector2ViewModel(slot);
            fieldView.initViewModel(viewModel);

            TextField field1 = new TextField();
            setSize(field1, (STD_WIDTH - 10) / 2, STD_HEIGHT);
            setFont(field1);
            setPadding(field1);
            field1.textProperty().bindBidirectional(viewModel.getXValueProperty());
            field1.borderProperty().bind(viewModel.getXBorderProperty());

            TextField field2 = new TextField();
            setSize(field2, (STD_WIDTH - 10) / 2, STD_HEIGHT);
            setFont(field2);
            setPadding(field2);
            field2.setLayoutX((STD_WIDTH - 10) / 2 + STD_MARGIN);
            field2.textProperty().bindBidirectional(viewModel.getYValueProperty());
            field2.borderProperty().bind(viewModel.getYBorderProperty());

            Pane pane = new Pane();
            setSize(pane, STD_WIDTH, STD_HEIGHT);
            pane.getChildren().add(field1);
            pane.getChildren().add(field2);
            pane.setManaged(true);
            return pane;
        } else if(slot.getProperty().getType() == DataType.VECTOR3) {
            Vector3ViewModel viewModel = new Vector3ViewModel(slot);
            fieldView.initViewModel(viewModel);

            TextField field1 = new TextField();
            setSize(field1, (STD_WIDTH - 20) / 3, STD_HEIGHT);
            setFont(field1);
            setPadding(field1);
            field1.textProperty().bindBidirectional(viewModel.getXValueProperty());
            field1.borderProperty().bind(viewModel.getXBorderProperty());

            TextField field2 = new TextField();
            setSize(field2, (STD_WIDTH - 20) / 3, STD_HEIGHT);
            setFont(field2);
            setPadding(field2);
            field2.setLayoutX((STD_WIDTH - 20) / 3 + STD_MARGIN);
            field2.textProperty().bindBidirectional(viewModel.getYValueProperty());
            field2.borderProperty().bind(viewModel.getYBorderProperty());

            TextField field3 = new TextField();
            setSize(field3, (STD_WIDTH - 20) / 3, STD_HEIGHT);
            setFont(field3);
            setPadding(field3);
            field3.setLayoutX((STD_WIDTH - 20) / 3 * 2 + STD_MARGIN * 2);
            field3.textProperty().bindBidirectional(viewModel.getZValueProperty());
            field3.borderProperty().bind(viewModel.getZBorderProperty());

            Pane pane = new Pane();
            setSize(pane, STD_WIDTH, STD_HEIGHT);
            pane.getChildren().add(field1);
            pane.getChildren().add(field2);
            pane.getChildren().add(field3);
            pane.setManaged(true);
            return pane;
        } else if(slot.getProperty().getType() == DataType.VECTOR4) {
            Vector4ViewModel viewModel = new Vector4ViewModel(slot);
            fieldView.initViewModel(viewModel);

            TextField field1 = new TextField();
            setSize(field1, (STD_WIDTH - 30) / 4, STD_HEIGHT);
            setFont(field1);
            setPadding(field1);
            field1.textProperty().bindBidirectional(viewModel.getXValueProperty());
            field1.borderProperty().bind(viewModel.getXBorderProperty());

            TextField field2 = new TextField();
            setSize(field2, (STD_WIDTH - 30) / 4, STD_HEIGHT);
            setFont(field2);
            setPadding(field2);
            field2.setLayoutX((STD_WIDTH - 30) / 4 + STD_MARGIN);
            field2.textProperty().bindBidirectional(viewModel.getYValueProperty());
            field2.borderProperty().bind(viewModel.getYBorderProperty());

            TextField field3 = new TextField();
            setSize(field3, (STD_WIDTH - 30) / 4, STD_HEIGHT);
            setFont(field3);
            setPadding(field3);
            field3.setLayoutX((STD_WIDTH - 30) / 4 * 2 + STD_MARGIN * 2);
            field3.textProperty().bindBidirectional(viewModel.getZValueProperty());
            field3.borderProperty().bind(viewModel.getZBorderProperty());

            TextField field4 = new TextField();
            setSize(field4, (STD_WIDTH - 30) / 4, STD_HEIGHT);
            setFont(field4);
            setPadding(field4);
            field4.setLayoutX((STD_WIDTH - 30) / 4 * 3 + STD_MARGIN * 3);
            field4.textProperty().bindBidirectional(viewModel.getWValueProperty());
            field4.borderProperty().bind(viewModel.getWBorderProperty());

            Pane pane = new Pane();
            setSize(pane, STD_WIDTH, STD_HEIGHT);
            pane.getChildren().add(field1);
            pane.getChildren().add(field2);
            pane.getChildren().add(field3);
            pane.getChildren().add(field4);
            pane.setManaged(true);
            return pane;
        }

        return null;
    }
}
