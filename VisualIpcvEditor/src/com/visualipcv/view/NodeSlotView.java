package com.visualipcv.view;

import com.visualipcv.controller.IGraphViewElement;
import com.visualipcv.core.NodeSlot;
import com.visualipcv.viewmodel.NodeSlotViewModel;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NodeSlotView extends AnchorPane implements IGraphViewElement {
    private NodeSlotViewModel viewModel;
    private NodeView node;

    private ObservableList<ConnectionView> connections = FXCollections.observableArrayList();
    private BooleanProperty isOutputProperty = new SimpleBooleanProperty();

    @FXML
    private Circle backgroundCircle;
    @FXML
    private Circle fillCircle;

    public NodeSlotView(NodeView node, NodeSlot slot) {
        viewModel = new NodeSlotViewModel(node.getViewModel(), slot);
        init(node);
    }

    public NodeSlotView(NodeView node, InputFieldView view, NodeSlot slot) {
        viewModel = new NodeSlotViewModel(node.getViewModel(), view.getViewModel(), slot);
        init(node);
    }

    private void init(NodeView node) {
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
        isOutputProperty.bind(viewModel.getIsOutputProperty());
    }

    public NodeSlotViewModel getViewModel() {
        return viewModel;
    }

    public NodeView getNode() {
        return node;
    }

    public List<ConnectionView> getConnections() {
        List<ConnectionView> connections = new ArrayList<>();

        for(ConnectionView view : node.getGraphView().getConnections()) {
            if(view.getSourceView() == this || view.getTargetView() == this)
                connections.add(view);
        }

        return connections;
    }

    public boolean isOutput() {
        return isOutputProperty.get();
    }

    public boolean isInput() {
        return !isOutputProperty.get();
    }

    @FXML
    public void onMousePressed(MouseEvent event) {
        event.consume();
    }

    @FXML
    public void onMouseDragged(MouseEvent event) {
        event.consume();
    }

    private void startConnectionDrag(MouseEvent event, NodeSlotView source) {
        Dragboard dragboard = source.startDragAndDrop(TransferMode.LINK);
        ClipboardContent content = new ClipboardContent();
        content.putString("Link");
        dragboard.setContent(content);
        event.consume();

        getGraphView().startConnectionDrag(source);
    }

    @FXML
    public void onDragDetected(MouseEvent event) {
        if(isOutput()) {
            startConnectionDrag(event, this);
        } else if(isInput()) {
            List<ConnectionView> connections = getConnections();

            if(connections.isEmpty()) {
                startConnectionDrag(event, this);
            } else {
                NodeSlotView source = null;
                viewModel.disconnect();

                if(connections.get(0).getSourceView() == this)
                    source = connections.get(0).getTargetView();
                else
                    source = connections.get(0).getSourceView();

                startConnectionDrag(event, source);
            }
        }
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

    @Override
    public GraphView getGraphView() {
        return getNode().getGraphView();
    }
}