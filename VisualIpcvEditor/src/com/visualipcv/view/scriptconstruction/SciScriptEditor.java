package com.visualipcv.view.scriptconstruction;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class SciScriptEditor extends AnchorPane {
    private PropertyStackView inputProperties;
    private PropertyStackView outputProperties;
    private TextArea codeArea;

    public SciScriptEditor() {
        SplitPane pane1 = new SplitPane();
        pane1.setOrientation(Orientation.HORIZONTAL);
        VBox pane2 = new VBox();
        pane1.getItems().add(pane2);

        VBox codePane = new VBox();

        Button createButton = new Button("Save");
        VBox.setMargin(createButton, new Insets(3.0));
        codePane.getChildren().add(createButton);

        codeArea = new TextArea();
        VBox.setMargin(codeArea, new Insets(3.0));
        VBox.setVgrow(codeArea, Priority.ALWAYS);
        codePane.getChildren().add(codeArea);

        inputProperties = new PropertyStackView("Input properties");
        outputProperties = new PropertyStackView("Output properties");

        pane2.getChildren().add(inputProperties);
        pane2.getChildren().add(outputProperties);
        pane1.getItems().add(codePane);

        pane1.setDividerPosition(0, 0.2);
        getChildren().add(pane1);

        setLeftAnchor(pane1, 0.0);
        setRightAnchor(pane1, 0.0);
        setTopAnchor(pane1, 0.0);
        setBottomAnchor(pane1, 0.0);
    }
}
