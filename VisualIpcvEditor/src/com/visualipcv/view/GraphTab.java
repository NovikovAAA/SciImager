package com.visualipcv.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Tab;

public class GraphTab extends Tab {
    private GraphView graphView;
    private String filePath;

    public GraphTab(GraphView view, String filePath) {
        super("", view);
        this.graphView = view;
        this.filePath = filePath;
        textProperty().bind(graphView.getViewModel().getNameProperty());
    }

    public GraphView getGraphView() {
        return graphView;
    }

    public String getFilePath() {
        return filePath;
    }
}
