package com.visualipcv.controller.scriptconstruction;

import com.visualipcv.controller.Controller;
import com.visualipcv.controller.INameable;
import com.visualipcv.controller.binding.Binder;
import com.visualipcv.controller.binding.UIProperty;
import com.visualipcv.core.Document;
import com.visualipcv.core.DocumentManager;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorLibrary;
import com.visualipcv.core.SciProcessor;
import com.visualipcv.core.io.SciScriptEntity;
import com.visualipcv.editor.Editor;
import com.visualipcv.editor.EditorCommand;
import com.visualipcv.editor.EditorWindow;
import com.visualipcv.scripts.SciScript;
import com.visualipcv.view.docking.DockPos;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

@EditorWindow(path = "", name = "Script", dockPos = DockPos.CENTER, prefWidth = 1280.0, prefHeight = 720.0)
public class SciScriptEditor extends Controller<AnchorPane> implements INameable {
    @FXML
    private TextArea codeField;
    @FXML
    private VBox properties;

    private UIProperty nameProperty = new UIProperty();

    private PropertyStackController inputProperties = new PropertyStackController("Input properties");
    private PropertyStackController outputProperties = new PropertyStackController("Output properties");

    @EditorCommand(path = "Editor/New script")
    public static void createNewScriptCommand() {
        Document doc = DocumentManager.getActiveDocument();

        if(doc == null) {
            doc = DocumentManager.createDocument();
        }

        SciScript script = doc.addScript();
        Editor.openWindow(new SciScriptEditor(script));
    }

    public SciScriptEditor(SciScript script) {
        super(AnchorPane.class, "SciScriptEditor.fxml");

        setContext(script);

        properties.getChildren().add(inputProperties.getView());
        properties.getChildren().add(outputProperties.getView());

        inputProperties.getView().maxHeightProperty().bind(properties.heightProperty().multiply(0.4));
        outputProperties.getView().maxHeightProperty().bind(properties.heightProperty().multiply(0.4));

        nameProperty.setBinder(context -> ((SciScript)context).getName());

        codeField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ((SciScript)getContext()).setCode(newValue);
            }
        });
    }

    @Override
    public void setContext(Object context) {
        super.setContext(context);
        inputProperties.setContext(((SciScript)context).getInputProperties());
        outputProperties.setContext(((SciScript)context).getOutputProperties());
        codeField.setText(((SciScript)context).getCode());
    }

    @Override
    public UIProperty nameProperty() {
        return nameProperty;
    }
}
