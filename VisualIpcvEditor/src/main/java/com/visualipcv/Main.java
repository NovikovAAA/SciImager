package com.visualipcv;

import com.visualipcv.core.ProcessorLibrary;
import com.visualipcv.editor.Editor;
import com.visualipcv.scripts.SciRunner;
import com.visualipcv.utils.LinkUtils;
import com.visualipcv.view.NormalStage;
import javafx.application.Application;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;
import java.util.logging.Logger;

public class Main extends Application {
    private static final Logger logger = Logger.getLogger(Main.class.toString());

    public static void start(String[] args) throws IOException {
        launch(args);
    }

    private TreeView<String> generateRandomTree() {
        TreeItem<String> root = new TreeItem<String>("Root");
        TreeView<String> treeView = new TreeView<String>(root);
        treeView.setShowRoot(false);

        Random rand = new Random();
        for (int i = 4 + rand.nextInt(8); i > 0; i--) {
            TreeItem<String> treeItem = new TreeItem<String>("Item " + i);
            root.getChildren().add(treeItem);
            for (int j = 2 + rand.nextInt(4); j > 0; j--) {
                TreeItem<String> childItem = new TreeItem<String>("Child " + j);
                treeItem.getChildren().add(childItem);
            }
        }

        return treeView;
    }

    @Override
    public void start(Stage primaryStage) {
        Editor.initPrimaryStage(primaryStage);
        NormalStage.addPrimaryStage(primaryStage);
    }
}
