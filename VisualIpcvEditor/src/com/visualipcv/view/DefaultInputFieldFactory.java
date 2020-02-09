package com.visualipcv.view;

import com.visualipcv.core.DataType;
import com.visualipcv.utils.UIHighlight;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import sun.plugin.util.UIUtil;

public class DefaultInputFieldFactory extends InputFieldFactory {
    @Override
    public Node create(InputFieldView fieldView, DataType type) {
        if(type == DataType.NUMBER) {
            TextField field = new TextField();
            field.setPrefWidth(180.0);
            field.setPrefHeight(15.0);
            field.setMaxWidth(180.0);
            field.setMaxHeight(15.0);
            field.setFont(new Font(14.0));
            field.setPadding(new Insets(3.0));
            field.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    try {
                        double value = Double.parseDouble(newValue);
                        fieldView.getValueProperty().set(value);
                        UIHighlight.removeHighlight(field);
                    } catch(Exception e) {
                        UIHighlight.highlight(field, Color.RED);
                    }
                }
            });
            return field;
        } else if(type == DataType.STRING) {
            TextField field = new TextField();
            field.setPrefWidth(180.0);
            field.setPrefHeight(15.0);
            field.setMaxWidth(180.0);
            field.setMaxHeight(15.0);
            field.setFont(new Font(14.0));
            field.setPadding(new Insets(3.0));
            field.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    fieldView.getValueProperty().set(field.getText());
                }
            });
            return field;
        }

        return null;
    }
}
