package com.visualipcv.view;

import com.visualipcv.core.DataType;
import com.visualipcv.core.ProcessorLibrary;
import com.visualipcv.core.ProcessorProperty;
import com.visualipcv.core.SciProcessor;
import com.visualipcv.scripts.SciScript;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class ScriptEditorView extends AnchorPane {
    @FXML
    private Button saveButton;
    @FXML
    private TextField nameField;
    @FXML
    private TextArea codeField;

    public ScriptEditorView() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ScriptEditor.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SciScript script = new SciScript();
                script.setCode(codeField.getText());
                script.addInputProperty(new ProcessorProperty("A", DataType.NUMBER));
                script.addInputProperty(new ProcessorProperty("B", DataType.NUMBER));
                script.addOutputProperty(new ProcessorProperty("Result", DataType.NUMBER));

                SciProcessor processor = new SciProcessor(nameField.getText(), "Custom", "Custom", script);
                ProcessorLibrary.addProcessor(processor);
            }
        });
    }
}
