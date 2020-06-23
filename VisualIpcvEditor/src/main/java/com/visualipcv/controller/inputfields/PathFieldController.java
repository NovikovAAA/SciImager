package com.visualipcv.controller.inputfields;

import com.visualipcv.controller.BorderUtils;
import com.visualipcv.controller.Controller;
import com.visualipcv.controller.InputFieldController;
import com.visualipcv.controller.binding.PropertyChangedEventListener;
import com.visualipcv.controller.binding.UIProperty;
import com.visualipcv.core.InputNodeSlot;
import com.visualipcv.core.NodeSlot;
import com.visualipcv.core.ValidationException;
import com.visualipcv.editor.Editor;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;

public class PathFieldController extends Controller<Pane> {
    private TextField pathField;
    private Button selectButton;
    private UIProperty pathProperty = new UIProperty();

    public PathFieldController(boolean isDirectory) {
        super(Pane.class);

        pathField = new TextField();
        pathField.setPrefSize(InputFieldController.STD_WIDTH - 60, InputFieldController.STD_HEIGHT);
        pathField.setMinSize(pathField.getPrefWidth(), pathField.getPrefHeight());
        pathField.setMaxSize(pathField.getPrefWidth(), pathField.getPrefHeight());
        pathField.setPadding(new Insets(InputFieldController.STD_TEXT_PADDING));
        pathField.setEditable(true);

        selectButton = new Button();
        selectButton.setPrefSize(50.0, InputFieldController.STD_HEIGHT);
        selectButton.setMinSize(selectButton.getPrefWidth(), selectButton.getPrefHeight());
        selectButton.setMaxSize(selectButton.getPrefWidth(), selectButton.getPrefHeight());
        selectButton.setLayoutX(InputFieldController.STD_WIDTH - 50.0);
        selectButton.setLayoutY(0.0);
        selectButton.setText("...");

        getView().getChildren().add(pathField);
        getView().getChildren().add(selectButton);

        pathProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                pathField.setText(newValue.toString());
            }
        });

        pathProperty.setBinder(context -> InputFieldController.getValueFromSlot((NodeSlot)context));

        pathField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onAction();
            }
        });

        pathField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                onAction();
            }
        });

        pathField.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if(event.getDragboard().hasFiles()) {
                    event.acceptTransferModes(TransferMode.ANY);
                    event.consume();
                }
            }
        });

        pathField.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if(event.getDragboard().hasFiles()) {
                    try {
                        String path = event.getDragboard().getFiles().get(0).getPath();
                        ((InputNodeSlot)getContext()).setValue(path);
                        pathField.setBorder(null);
                        poll(pathProperty);
                    } catch (ValidationException e) {
                        pathField.setBorder(BorderUtils.createErrorBorder());
                    }

                    event.consume();
                }
            }
        });

        selectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File file;

                if(isDirectory) {
                    DirectoryChooser chooser = new DirectoryChooser();
                    chooser.setTitle("Open directory");
                    file = chooser.showDialog(Editor.getPrimaryStage());
                } else {
                    FileChooser chooser = new FileChooser();
                    chooser.setTitle("Open file");
                    file = chooser.showOpenDialog(Editor.getPrimaryStage());
                }

                if(file != null) {
                    try {
                        ((InputNodeSlot)getContext()).setValue(file.getAbsolutePath());
                        pathField.setBorder(null);
                        poll(pathProperty);
                    } catch (ValidationException e) {
                        pathField.setBorder(BorderUtils.createErrorBorder());
                    }
                }
            }
        });

        initialize();
    }

    private void onAction() {
        try {
            ((InputNodeSlot)getContext()).setValue(pathField.getText());
            pathField.setBorder(null);
        } catch(ValidationException e) {
            pathField.setBorder(BorderUtils.createErrorBorder());
        }
    }
}
