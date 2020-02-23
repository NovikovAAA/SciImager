package com.visualipcv.view;

import com.visualipcv.editor.Editor;
import com.visualipcv.viewmodel.GraphTabPaneViewModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.FileChooser;

import java.io.File;

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
            ((GraphView)tab.getContent()).onSave(tab.getFilePath());
        }
    }

    public void saveAs() {

    }

    public void load() {

    }
}
