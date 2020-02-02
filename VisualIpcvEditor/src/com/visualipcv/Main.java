package com.visualipcv;

import com.visualipcv.core.ProcessorLibrary;
import com.visualipcv.utils.LinkUtils;
import com.visualipcv.view.FunctionListView;
import com.visualipcv.view.NodeSlotView;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;

import java.io.IOException;
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

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("VisualIPCV");

        SplitPane root = new SplitPane();
        root.setOrientation(Orientation.HORIZONTAL);
        root.getItems().add(new FunctionListView());
        root.getItems().add(new NodeSlotView());

        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
    }
}
