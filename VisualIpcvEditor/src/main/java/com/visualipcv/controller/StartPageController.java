package com.visualipcv.controller;

import com.visualipcv.core.Document;
import com.visualipcv.core.DocumentManager;
import com.visualipcv.editor.Editor;
import com.visualipcv.editor.EditorWindow;
import com.visualipcv.view.docking.DockPos;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

@EditorWindow(path = "", name = "Welcome", dockPos = DockPos.CENTER, prefWidth = 1000.0, prefHeight = 700.0)
public class StartPageController extends Controller<AnchorPane> {
    @FXML
    private Button createButton;

    public StartPageController() {
        super(AnchorPane.class, "StartPage.fxml");

        createButton.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Document document = DocumentManager.createDocument();
                Editor.openWindow(new GraphController(document.addGraph()), StartPageController.class);
            }
        });
    }
}
