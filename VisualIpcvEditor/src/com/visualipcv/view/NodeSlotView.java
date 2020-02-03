package com.visualipcv.view;

import com.visualipcv.core.Node;
import com.visualipcv.core.NodeSlot;
import com.visualipcv.core.ProcessorProperty;
import com.visualipcv.viewmodel.NodeSlotViewModel;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Control;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.ArrayList;

public class NodeSlotView extends AnchorPane {
    private NodeSlotViewModel viewModel;

    @FXML
    private Circle backgroundCircle;
    @FXML
    private Circle fillCircle;

    public NodeSlotView(NodeSlot slot) {
        viewModel = new NodeSlotViewModel(slot);

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("NodeSlotView.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch(IOException e) {
            e.printStackTrace();
        }

        fillCircle.visibleProperty().bind(viewModel.getIsConnectedProperty());
        fillCircle.fillProperty().bind(viewModel.getStrokeProperty());
        backgroundCircle.fillProperty().bind(viewModel.getBackgroundProperty());
        backgroundCircle.strokeProperty().bind(viewModel.getStrokeProperty());
    }
}