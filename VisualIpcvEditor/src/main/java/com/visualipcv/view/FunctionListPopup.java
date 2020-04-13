package com.visualipcv.view;

import com.visualipcv.controller.FunctionListController;
import com.visualipcv.controller.GraphController;
import com.visualipcv.core.Graph;
import com.visualipcv.editor.Editor;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FunctionListPopup extends Stage {
    private FunctionListController functionListController;

    public FunctionListPopup(GraphController graph, double graphX, double graphY, double screenX, double screenY) {
        functionListController = new FunctionListController(graph, graphX, graphY);

        BorderPane root = new BorderPane();
        root.setPrefWidth(320.0);
        root.setPrefHeight(320.0);
        root.setPadding(new Insets(10.0));
        root.setStyle("-fx-background-color: transparent;");

        AnchorPane content = new AnchorPane();
        content.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 10,0.2,0,0);");
        content.setPrefWidth(300.0);
        content.setPrefHeight(300.0);
        content.getChildren().add(functionListController.getView());
        root.setCenter(content);

        AnchorPane.setBottomAnchor(functionListController.getView(), 0.0);
        AnchorPane.setTopAnchor(functionListController.getView(), 0.0);
        AnchorPane.setLeftAnchor(functionListController.getView(), 0.0);
        AnchorPane.setRightAnchor(functionListController.getView(), 0.0);

        functionListController.disableAddButton();
        setScene(new Scene(root));
        getScene().setFill(Color.TRANSPARENT);
        initStyle(StageStyle.TRANSPARENT);
        initModality(Modality.NONE);
        initOwner(Editor.getPrimaryStage());

        setX(screenX);
        setY(screenY);

        focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue) {
                    hide();
                }
            }
        });

        addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER) {
                    hide();
                }
            }
        });

        addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2) {
                    hide();
                }
            }
        });
    }
}
