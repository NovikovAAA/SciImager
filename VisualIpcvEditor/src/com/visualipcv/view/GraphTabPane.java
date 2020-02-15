package com.visualipcv.view;

import com.visualipcv.editor.Editor;
import com.visualipcv.viewmodel.GraphTabPaneViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

public class GraphTabPane extends TabPane {
    private GraphTabPaneViewModel viewModel = new GraphTabPaneViewModel();

    private Tab createTabForGraph(GraphView view) {
        Tab tab = new Tab();
        tab.setContent(view);
        tab.textProperty().bind(view.getViewModel().getNameProperty());
        return tab;
    }

    public void createNew() {
        getTabs().add(new GraphTab(new GraphView(), null));
    }

    public void close() {
        getTabs().remove(getSelectionModel().getSelectedItem());
    }

    public void save() {
        GraphTab tab = (GraphTab)getSelectionModel().getSelectedItem();

        if(tab.getFilePath() == null) {
            saveAs();
        } else {
            ((GraphView)tab.getContent()).save(tab.getFilePath());
        }
    }

    public void saveAs() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save");
        chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("VisualIPCV graph", ".vip"));
        File file = chooser.showSaveDialog(Editor.getPrimaryStage());

        try {
            if(file != null) {
                GraphView view = (GraphView)getSelectionModel().getSelectedItem().getContent();

                if(view != null) {
                    view.save(file.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open");
        chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("VisualIPCV graph", ".vip"));
        File file = chooser.showOpenDialog(Editor.getPrimaryStage());

        try {
            if(file != null) {
                GraphView view = new GraphView();
                view.load(file.getAbsolutePath());
                getTabs().add(new GraphTab(view, file.getAbsolutePath()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
