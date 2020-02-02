package com.visualipcv.view;

import com.visualipcv.core.Node;
import com.visualipcv.viewmodel.NodeViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;

public class NodeView extends AnchorPane {
    private NodeViewModel viewModel;

    @FXML
    private Text title;

    private double previousMouseX = 0.0;
    private double previousMouseY = 0.0;

    private ObservableList<NodeSlotView> inputSlots = FXCollections.observableArrayList();
    private ObservableList<NodeSlotView> outputSlots = FXCollections.observableArrayList();

    public NodeView(Node node) {
        viewModel = new NodeViewModel(node);

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("NodeView.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch(IOException e) {
            e.printStackTrace();
        }

        layoutXProperty().bindBidirectional(viewModel.getLayoutXProperty());
        layoutYProperty().bindBidirectional(viewModel.getLayoutYProperty());
        title.textProperty().bind(viewModel.getTitleProperty());
    }

    @FXML
    public void onMousePressed(MouseEvent event) {
        previousMouseX = event.getScreenX();
        previousMouseY = event.getScreenY();
        event.consume();
    }

    @FXML
    public void onMouseDragged(MouseEvent event) {
        double deltaX = event.getScreenX() - previousMouseX;
        double deltaY = event.getScreenY() - previousMouseY;
        previousMouseX = event.getScreenX();
        previousMouseY = event.getScreenY();
        layoutXProperty().set(getLayoutX() + deltaX);
        layoutYProperty().set(getLayoutY() + deltaY);
        event.consume();
    }
}
