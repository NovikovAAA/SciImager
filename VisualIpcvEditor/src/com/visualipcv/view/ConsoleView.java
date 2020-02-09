package com.visualipcv.view;

import com.visualipcv.Console;
import com.visualipcv.editor.EditorWindow;
import com.visualipcv.events.ConsoleEventListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.dockfx.DockPos;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

@EditorWindow(path="View/Console", name="Console", dockPos = DockPos.BOTTOM)
public class ConsoleView extends AnchorPane {
    @FXML
    private TextField inputField;
    @FXML
    private TextArea output;
    @FXML
    private Button clearButton;

    public ConsoleView() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ConsoleView.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch(IOException e) {
            e.printStackTrace();
        }

        clearButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                output.clear();
            }
        });

        Console.addEventListener(new ConsoleEventListener() {
            @Override
            public void onRecordAdded(String text) {

            }

            @Override
            public void onCmdWritten(String cmd) {
                output.appendText(">> " + cmd + "\n");
            }

            @Override
            public void onResponse(String response) {
                output.appendText(response + "\n");
            }
        });
    }

    @FXML
    public void onClear(ActionEvent event) {
        output.clear();
    }

    @FXML
    public void onKeyReleased(KeyEvent event) {

    }
}
