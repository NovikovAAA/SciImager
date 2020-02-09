package com.visualipcv;

import com.visualipcv.core.ProcessorLibrary;
import com.visualipcv.utils.LinkUtils;
import com.visualipcv.view.ConsoleView;
import com.visualipcv.view.FunctionListView;
import com.visualipcv.view.GraphView;
import com.visualipcv.view.NodeSlotView;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.concurrent.CancellationException;
import java.util.logging.Logger;

public class Main extends Application {

    static {
        LinkUtils.linkNativeLibraries();
    }

    private static ProcessorLibrary processorLibrary = new ProcessorLibrary();
    private static final Logger logger = Logger.getLogger(Main.class.toString());

    public static void main(String[] args) throws IOException {
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
        primaryStage.setTitle("VisualIPCV");

        VBox root = new VBox();
        root.getChildren().addAll();

        SplitPane hPane = new SplitPane();
        hPane.setOrientation(Orientation.HORIZONTAL);

        SplitPane vPane = new SplitPane();
        vPane.setOrientation(Orientation.VERTICAL);

        hPane.getItems().add(new FunctionListView());
        hPane.getItems().add(vPane);
        vPane.getItems().add(new GraphView());
        vPane.getItems().add(new ConsoleView());

        root.getChildren().add(hPane);

        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.sizeToScene();
        primaryStage.show();
    }
}
