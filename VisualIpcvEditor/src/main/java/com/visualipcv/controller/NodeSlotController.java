package com.visualipcv.controller;

import com.visualipcv.controller.binding.PropertyChangedEventListener;
import com.visualipcv.controller.binding.UIProperty;
import com.visualipcv.core.DataType;
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
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class NodeSlotController extends Controller<NodeSlotView> {
    private NodeController nodeController;
    private static NodeSlotController draggingSlot;

    @FXML
    private Circle backgroundCircle;
    @FXML
    private Circle fillCircle;
    @FXML
    private Rectangle backgroundRectangle;
    @FXML
    private Rectangle fillRectangle;

    private UIProperty connectedProperty = new UIProperty();
    private UIProperty connectionsProperty = new UIProperty();
    private UIProperty backgroundColorProperty = new UIProperty();
    private UIProperty foregroundColorProperty = new UIProperty();
    private UIProperty isOutputProperty = new UIProperty();
    private UIProperty isArrayProperty = new UIProperty();

    public NodeSlotController(NodeController controller) {
        super(NodeSlotView.class, "NodeSlotView.fxml");
        getView().setController(this);

        this.nodeController = controller;

        connectedProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                if(!((Boolean) newValue)) {
                    fillCircle.setVisible((Boolean)newValue);
                    fillRectangle.setVisible(false);
                }
                else {
                    fillRectangle.setVisible((Boolean)newValue);
                    fillCircle.setVisible(false);
                }
            }
        });

        backgroundColorProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                backgroundCircle.fillProperty().set((Color)newValue);
                backgroundRectangle.fillProperty().set((Color)newValue);
            }
        });

        foregroundColorProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                backgroundCircle.strokeProperty().set((Color)newValue);
                fillCircle.fillProperty().set((Color)newValue);
                backgroundRectangle.strokeProperty().set((Color)newValue);
                fillRectangle.fillProperty().set((Color)newValue);
            }
        });

        isArrayProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                if((Boolean)newValue) {
                    backgroundCircle.setVisible(false);
                    backgroundRectangle.setVisible(true);
                } else {
                    backgroundCircle.setVisible(true);
                    backgroundRectangle.setVisible(false);
                }
            }
        });

        connectedProperty.setBinder((Object slot) -> {
            return ((NodeSlot)slot).isConnected();
        });

        backgroundColorProperty.setBinder((Object slot) -> {
            Color color = ((NodeSlot)slot).getActualType().getColor();
            return new Color(color.getRed() * 0.2, color.getGreen() * 0.2, color.getBlue() * 0.2, 1.0);
        });

        foregroundColorProperty.setBinder((Object slot) -> {
            return ((NodeSlot)slot).getActualType().getColor();
        });

        isOutputProperty.setBinder((Object slot) -> {
            return slot instanceof OutputNodeSlot;
        });

        isArrayProperty.setBinder((Object slot) -> {
            return ((NodeSlot)slot).getProperty().isArray();
        });

        initialize();
    }

    // TODO: need optimization
    public List<ConnectionController> getConnections() {
        List<ConnectionController> connections = new ArrayList<>();

        for(ConnectionController connection : getNodeController().getGraphController().getConnections()) {
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

        draggingSlot = source;
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

                if(connections.get(0).getSourceSlot() == this)
                    source = connections.get(0).getTargetSlot();
                else
                    source = connections.get(0).getSourceSlot();

                startConnectionDrag(event, source);
                ((NodeSlot)getContext()).disconnect();
                nodeController.getGraphController().invalidate();
            }
        }
    }

    @FXML
    public void onDragOver(DragEvent event) {
        if(draggingSlot == null)
            return;

        NodeSlot slot1 = draggingSlot.getContext();
        NodeSlot slot2 = getContext();

        if(NodeSlot.isConnectionAvailable(slot1, slot2)) {
            event.acceptTransferModes(TransferMode.ANY);
            event.consume();
        }
    }

    @FXML
    public void onDragDropped(DragEvent event) {
        if(!event.getDragboard().getString().equals("Slot"))
            return;

        if(draggingSlot == null)
            return;

        if(!NodeSlot.isConnectionAvailable(draggingSlot.getContext(), getContext()))
            return;

        NodeSlotController slotController = draggingSlot;
        ((NodeSlot)getContext()).connect(slotController.getContext());
        nodeController.getGraphController().invalidate();

        event.setDropCompleted(true);
        event.consume();
        draggingSlot = null;
    }

    public NodeController getNodeController() {
        return nodeController;
    }

    public UIProperty connectedProperty() {
        return connectedProperty;
    }

    public UIProperty isOutputProperty() {
        return isOutputProperty;
    }
}