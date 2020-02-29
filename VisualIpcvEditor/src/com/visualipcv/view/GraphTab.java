package com.visualipcv.view;

import com.visualipcv.controller.GraphController;
import javafx.scene.control.Tab;

public class GraphTab extends Tab {
    private GraphController graphController;
    private String filePath;

    public GraphTab(GraphController graphController, String filePath) {
        super("", graphController.getView());
        this.graphController = graphController;
        this.filePath = filePath;
        setText("");
    }

    public GraphController getGraphController() {
        return graphController;
    }

    public String getFilePath() {
        return filePath;
    }
}
