package com.visualipcv.controller.scriptconstruction;

import com.visualipcv.controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class SciScriptEditor extends Controller<AnchorPane> {
    @FXML
    private TextField codeField;
    @FXML
    private VBox properties;

    private PropertyStackController inputProperties = new PropertyStackController("Input properties");
    private PropertyStackController outputProperties = new PropertyStackController("Output properties");

    public SciScriptEditor() {
        super(AnchorPane.class, "SciScriptEditor.fxml");

        inputProperties.setContext(new ArrayList<>());
        outputProperties.setContext(new ArrayList<>());
    }
}
