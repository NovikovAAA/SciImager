package com.visualipcv.controller;

import com.visualipcv.Console;
import com.visualipcv.controller.Controller;
import com.visualipcv.editor.EditorWindow;
import com.visualipcv.events.ConsoleEventListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import com.visualipcv.view.docking.DockPos;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

@EditorWindow(path="View/Console", name="Console", dockPos = DockPos.BOTTOM)
public class ConsoleController extends Controller<AnchorPane> {
    @FXML
    private TextField inputField;
    @FXML
    private TextArea output;
    @FXML
    private Button clearButton;

    public ConsoleController() {
        super(AnchorPane.class, "ConsoleView.fxml");

        clearButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                output.clear();
            }
        });

        inputField.setOnKeyReleased(new EventHandler<javafx.scene.input.KeyEvent>() {
            @Override
            public void handle(javafx.scene.input.KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER) {
                    Console.execute(inputField.getText(), true);
                    inputField.clear();
                }
            }
        });

        Console.addEventListener(new ConsoleEventListener() {
            @Override
            public void onUpdate(String text) {
                output.setText(text);
                output.setScrollTop(Double.MAX_VALUE);
            }
        });
    }
}
