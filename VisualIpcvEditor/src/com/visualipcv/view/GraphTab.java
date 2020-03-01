package com.visualipcv.view;

import com.visualipcv.controller.GraphController;
import com.visualipcv.controller.binding.PropertyChangedEventListener;
import javafx.scene.control.Tab;

public class GraphTab extends Tab {
    private GraphController graphController;

    public GraphTab(GraphController graphController) {
        super("", graphController.getView());
        this.graphController = graphController;

        this.graphController.filePathProperty().addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                String name = (String)newValue;
                name = name.substring(Math.max(name.lastIndexOf('\\'), name.lastIndexOf('/')) + 1, name.length());
                setText(name);
            }
        });
    }

    public GraphController getGraphController() {
        return graphController;
    }
}
