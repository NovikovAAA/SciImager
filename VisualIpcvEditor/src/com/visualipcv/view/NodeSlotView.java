package com.visualipcv.view;

import com.visualipcv.core.ProcessorProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Control;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.ArrayList;

public class NodeSlotView extends AnchorPane {
    public NodeSlotView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("NodeSlotView.fxml"));

        try {
            fxmlLoader.load();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}