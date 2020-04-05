package com.visualipcv.view;

import com.visualipcv.controller.InputFieldController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class SearchListView<T> extends AnchorPane {
    private TextField searchField;
    private TreeView<T> tree;
    private TreeItem<T> originalRoot;

    public SearchListView() {
        searchField = new TextField();
        AnchorPane.setTopAnchor(searchField, 3.0);
        AnchorPane.setLeftAnchor(searchField, 3.0);
        AnchorPane.setRightAnchor(searchField, 3.0);
        searchField.setPromptText("Search");
        searchField.setPrefHeight(InputFieldController.STD_HEIGHT);
        searchField.setMaxHeight(InputFieldController.STD_HEIGHT);
        searchField.setMinHeight(InputFieldController.STD_HEIGHT);
        searchField.setStyle("-fx-border-radius: 10.0; -fx-background-radius: 10.0");

        tree = new TreeView<>();
        tree.setStyle("-fx-background-color: transparent;");
        AnchorPane.setBottomAnchor(tree, 0.0);
        AnchorPane.setRightAnchor(tree, 0.0);
        AnchorPane.setLeftAnchor(tree, 0.0);
        AnchorPane.setTopAnchor(tree, 6.0 + searchField.getPrefHeight());

        getChildren().add(searchField);
        getChildren().add(tree);

        searchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updateTree();
            }
        });

        searchField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.DOWN) {
                    getTree().requestFocus();
                    getTree().getSelectionModel().select(0);
                }
            }
        });

        tree.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.UP && tree.getSelectionModel().getSelectedIndex() == 0) {
                    searchField.requestFocus();
                }
            }
        });

        tree.setShowRoot(false);
    }

    public TreeView<T> getTree() {
        return tree;
    }

    public void setRoot(TreeItem<T> root) {
        this.originalRoot = root;
        updateTree();
    }

    private TreeItem<T> copyTreeItemAndFilter(TreeItem<T> item, boolean filter) {
        TreeItem<T> newItem = new TreeItem<>();
        newItem.setValue(item.getValue());
        newItem.setGraphic(item.getGraphic());
        newItem.setExpanded(true);

        for(TreeItem<T> child : item.getChildren()) {
            if(child.isLeaf() && !child.getValue().toString().toLowerCase().contains(searchField.getText().toLowerCase()) &&
                !searchField.getText().isEmpty() && filter)
                continue;

            TreeItem<T> childCopy = copyTreeItemAndFilter(child, !child.getValue().toString().toLowerCase().contains(searchField.getText().toLowerCase()));

            if(!child.isLeaf() && childCopy.getChildren().isEmpty())
                continue;

            newItem.getChildren().add(childCopy);
        }

        return newItem;
    }

    private void updateTree() {
        TreeItem<T> filtered = copyTreeItemAndFilter(originalRoot, true);
        getTree().setRoot(filtered);
    }
}
