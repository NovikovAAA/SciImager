package com.visualipcv.view;

import com.visualipcv.core.Node;
import com.visualipcv.core.NodeSlot;
import com.visualipcv.core.ProcessorProperty;
import com.visualipcv.viewmodel.NodeSlotViewModel;
import com.visualipcv.viewmodel.NodeViewModel;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Control;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

import javax.sound.sampled.Clip;
import java.io.IOException;
import java.util.ArrayList;

public class NodeSlotView extends AnchorPane {
    private NodeSlotViewModel viewModel;
    private NodeView node;

    @FXML
    private Circle backgroundCircle;
    @FXML
    private Circle fillCircle;

    public NodeSlotView(NodeView node, NodeSlot slot) {
        viewModel = new NodeSlotViewModel(node.getViewModel(), slot);
        this.node = node;

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

    public NodeSlotViewModel getViewModel() {
        return viewModel;
    }

    public NodeView getNode() {
        return node;
    }

    @FXML
    public void onMousePressed(MouseEvent event) {
        event.consume();
    }

    @FXML
    public void onMouseDragged(MouseEvent event) {
        event.consume();
    }

    @FXML
    public void onDragDetected(MouseEvent event) {
        Dragboard dragboard = startDragAndDrop(TransferMode.LINK);
        ClipboardContent content = new ClipboardContent();
        content.putString("Link");
        dragboard.setContent(content);
        dragboard.setDragView(this.snapshot(null, null));
        event.consume();
    }

    @FXML
    public void onDragOver(DragEvent event) {
        if(event.getGestureSource() instanceof NodeSlotView) {
            event.acceptTransferModes(TransferMode.LINK);
            event.consume();
        }
    }

    @FXML
    public void onDragDropped(DragEvent event) {
        if(!(event.getGestureSource() instanceof NodeSlotView))
            return;

        NodeSlotView slotView = (NodeSlotView)event.getGestureSource();
        viewModel.connect(slotView.getViewModel());
        event.consume();
    }
}