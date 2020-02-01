package com.visualipcv;

/*import com.visualipcv.controller.GraphController;
import com.visualipcv.core.Graph;
import com.visualipcv.core.ProcessorLibrary;
import com.visualipcv.utils.LinkUtils;
import com.visualipcv.view.ConsoleView;
import com.visualipcv.view.FunctionListView;
import com.visualipcv.view.GraphView;*/
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.EventQueue;
import java.awt.Frame;

import java.io.IOException;
import java.util.logging.Logger;

public class Main extends Application {

    /*static {
        LinkUtils.linkNativeLibraries();
    }*/

    //private static ProcessorLibrary processorLibrary = new ProcessorLibrary();
    private static final Logger logger = Logger.getLogger(Main.class.toString());

    //private static GraphController controller;

    public static void main(String[] args) throws IOException {
        /*EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = buildFrame();
                JSplitPane split = new JSplitPane();
                split.setLeftComponent(new FunctionListView(processorLibrary));

                GraphView graphView = new GraphView();
                Graph graph = new Graph();
                controller = new GraphController(graph, graphView);

                JSplitPane split2 = new JSplitPane();
                split2.setTopComponent(graphView);
                split2.setBottomComponent(new ConsoleView());
                split2.setContinuousLayout(true);
                split2.setDividerLocation(700);
                split2.setOrientation(JSplitPane.VERTICAL_SPLIT);

                split.setRightComponent(split2);
                split.setContinuousLayout(true);
                split.setDividerLocation(300);
                frame.add(split);
                Frame frame = new Frame();
                frame.setSize(1280, 720);
            }
        });*/

        launch(args);
    }

    private static JFrame buildFrame() {
        JFrame frame = new JFrame();
        frame.setTitle("VisualIPCV");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);
        frame.setVisible(true);
        return frame;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World!");
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
}
