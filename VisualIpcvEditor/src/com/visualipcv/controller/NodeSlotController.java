package com.visualipcv.controller;

import com.visualipcv.controller.binding.PropertyChangedEventListener;
import com.visualipcv.controller.binding.UIProperty;
import com.visualipcv.core.NodeSlot;
import com.visualipcv.core.OutputNodeSlot;
import com.visualipcv.view.NodeSlotView;
import javafx.fxml.FXML;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

public class NodeSlotController extends Controller<NodeSlotView> {
    private NodeController nodeController;
    private static NodeSlotController draggingSlot;

    @FXML
    private Circle backgroundCircle;
    @FXML
    private Circle fillCircle;

    private UIProperty connectedProperty = new UIProperty();
    private UIProperty connectionsProperty = new UIProperty();
    private UIProperty backgroundColorProperty = new UIProperty();
    private UIProperty foregroundColorProperty = new UIProperty();
    private UIProperty isOutputProperty = new UIProperty();

    public NodeSlotController(NodeController controller) {
        super(NodeSlotView.class, "NodeSlotView.fxml");
        getView().setController(this);

        this.nodeController = controller;

        connectedProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                fillCircle.setVisible((Boolean)newValue);
            }
        });

        backgroundColorProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                backgroundCircle.fillProperty().set((Color)newValue);
            }
        });

        foregroundColorProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                backgroundCircle.strokeProperty().set((Color)newValue);
                fillCircle.fillProperty().set((Color)newValue);
            }
        });

        connectedProperty.setBinder((Object slot) -> {
            return ((NodeSlot)slot).isConnected();
        });

        backgroundColorProperty.setBinder((Object slot) -> {
            java.awt.Color color = ((NodeSlot)slot).getProperty().getType().getColor();
            return new Color(color.getRed() / 400.0, color.getGreen() / 400.0, color.getBlue() / 400.0, 1.0);
        });

        foregroundColorProperty.setBinder((Object slot) -> {
            java.awt.Color color = ((NodeSlot)slot).getProperty().getType().getColor();
            return new Color(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0, 1.0);
        });

        isOutputProperty.setBinder((Object slot) -> {
            return slot instanceof OutputNodeSlot;
        });

        initialize();
    }

    public List<ConnectionController> getConnections() {
        List<ConnectionController> connections = new ArrayList<>();

        for(ConnectionController connection : nodeController.getGraphController().getConnections()) {
            if(connection.getSourceSlot() == this || connection.getTargetSlot() == this)
                connections.add(connection);
        }

        return connections;
    }

    public boolean isOutput() {
        return (Boolean)isOutputProperty.getValue();
    }

    public boolean isInput() {
        return !isOutput();
    }

    @FXML
    public void onMousePressed(MouseEvent event) {
        event.consume();
    }

    @FXML
    public void onMouseDragged(MouseEvent event) {
        event.consume();
    }

    private void startConnectionDrag(MouseEvent event, NodeSlotController source) {
        Dragboard dragboard = source.getView().startDragAndDrop(TransferMode.LINK);
        ClipboardContent content = new ClipboardContent();
        content.putString("Slot");
        dragboard.setContent(content);
        event.consume();

        draggingSlot = this;
        nodeController.getGraphController().startConnectionDrag(source);
    }

    @FXML
    public void onDragDetected(MouseEvent event) {
        if(isOutput()) {
            startConnectionDrag(event, this);
        } else if(isInput()) {
            List<ConnectionController> connections = getConnections();

            if(connections.isEmpty()) {
                startConnectionDrag(event, this);
            } else {
                NodeSlotController source = null;
                ((NodeSlot)getContext()).disconnect();
                invalidate();

                if(connections.get(0).getSourceSlot() == this)
                    source = connections.get(0).getTargetSlot();
                else
                    source = connections.get(0).getSourceSlot();

                startConnectionDrag(event, source);
            }
        }
    }

    @FXML
    public void onDragOver(DragEvent event) {
        if(event.getDragboard().getString().equals("Slot") && draggingSlot != null) {
            event.acceptTransferModes(TransferMode.LINK);
            event.consume();
        }
    }

    @FXML
    public void onDragDropped(DragEvent event) {
        if(!event.getDragboard().getString().equals("Slot"))
            return;

        NodeSlotController slotController = (NodeSlotController)draggingSlot;
        ((NodeSlot)getContext()).connect(slotController.getContext());
        nodeController.getGraphController().invalidate();

        event.setDropCompleted(true);
        event.consume();
        draggingSlot = null;
    }
}