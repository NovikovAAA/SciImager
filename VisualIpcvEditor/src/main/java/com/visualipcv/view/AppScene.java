package com.visualipcv.view;

import javafx.scene.Parent;
import javafx.scene.Scene;

public class AppScene extends Scene {
    public AppScene(Parent root) {
        super(root);
        loadStyles();
    }

    public AppScene(Parent root, double width, double height) {
        super(root, width, height);
        loadStyles();
    }

    private void loadStyles() {
        getStylesheets().add(getClass().getResource("LightTheme.css").toExternalForm());
    }
}
