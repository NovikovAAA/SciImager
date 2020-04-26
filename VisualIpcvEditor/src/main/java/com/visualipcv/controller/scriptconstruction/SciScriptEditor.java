package com.visualipcv.controller.scriptconstruction;

import com.visualipcv.controller.Controller;
import com.visualipcv.controller.binding.UIProperty;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorLibrary;
import com.visualipcv.core.SciProcessor;
import com.visualipcv.editor.EditorWindow;
import com.visualipcv.scripts.SciScript;
import com.visualipcv.view.docking.DockPos;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Optional;

@EditorWindow(path = "Edit/New script", name = "Script", dockPos = DockPos.CENTER, prefWidth = 1280.0, prefHeight = 720.0)
public class SciScriptEditor extends Controller<AnchorPane> {
    @FXML
    private TextArea codeField;
    @FXML
    private VBox properties;
    @FXML
    private Button saveButton;

    private UIProperty nameProperty = new UIProperty();

    private PropertyStackController inputProperties = new PropertyStackController("Input properties");
    private PropertyStackController outputProperties = new PropertyStackController("Output properties");

    public SciScriptEditor() {
        super(AnchorPane.class, "SciScriptEditor.fxml");

        properties.getChildren().add(inputProperties.getView());
        properties.getChildren().add(outputProperties.getView());

        inputProperties.getView().maxHeightProperty().bind(properties.heightProperty().multiply(0.4));
        outputProperties.getView().maxHeightProperty().bind(properties.heightProperty().multiply(0.4));

        inputProperties.setContext(new ArrayList<>());
        outputProperties.setContext(new ArrayList<>());

        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(getContext() == null) {
                    TextInputDialog dialog = new TextInputDialog("New processor");
                    dialog.setTitle("Save processor");
                    Optional<String> result = dialog.showAndWait();

                    if(result.isPresent()) {
                        SciScript script = new SciScript();
                        script.getInputProperties().addAll(inputProperties.getContext());
                        script.getOutputProperties().addAll(outputProperties.getContext());
                        script.setCode(codeField.getText());
                        SciProcessor processor = new SciProcessor(result.get(), "Custom", "Custom", script);
                        ProcessorLibrary.addProcessor(processor);
                        setContext(processor);
                    }
                } else {
                    SciProcessor processor = (SciProcessor)getContext();
                    processor.getInputProperties().clear();
                    processor.getOutputProperties().clear();
                    processor.getInputProperties().addAll(inputProperties.getContext());
                    processor.getOutputProperties().addAll(outputProperties.getContext());
                    processor.getScript().setCode(codeField.getText());
                }
            }
        });
    }

    @Override
    public void setContext(Object context) {
        super.setContext(context);
        nameProperty.setValue(((SciProcessor)context).getName());
    }

    public UIProperty nameProperty() {
        return nameProperty;
    }
}
