package com.visualipcv.view;

import com.visualipcv.Console;
import com.visualipcv.events.ConsoleEventListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

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
