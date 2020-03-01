package com.visualipcv.controller;

import com.visualipcv.controller.binding.BindingHelper;
import com.visualipcv.controller.binding.PropertyChangedEventListener;
import com.visualipcv.controller.binding.UIProperty;
import com.visualipcv.core.InputNodeSlot;
import com.visualipcv.core.NativeProcessor;
import com.visualipcv.core.Node;
import com.visualipcv.core.NodeSlot;
import com.visualipcv.core.OutputNodeSlot;
import com.visualipcv.core.SciProcessor;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class NodeController extends Controller<AnchorPane> {
    private GraphController graphController;

    private double previousMouseX;
    private double previousMouseY;

    @FXML
    private Text title;
    @FXML
    private VBox inputContainer;
    @FXML
    private VBox outputContainer;
    @FXML
    private Text error;
    @FXML
    private Pane errorPane;
    @FXML
    private Pane nodeClass;

    private UIProperty selectedProperty = new UIProperty(false);
    private UIProperty titleProperty = new UIProperty();
    private UIProperty inputSlotsProperty = new UIProperty();
    private UIProperty outputSlotsProperty = new UIProperty();
    private UIProperty nodeClassProperty = new UIProperty();
    private UIProperty errorProperty = new UIProperty();
    private UIProperty xOffsetProperty = new UIProperty();
    private UIProperty yOffsetProperty = new UIProperty();

    public NodeController(GraphController controller) {
        super(AnchorPane.class, "NodeView.fxml");
        this.graphController = controller;

        titleProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                title.setText((String)newValue);
            }
        });

        inputSlotsProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                inputContainer.getChildren().clear();

                for(AdvancedNodeSlotController slot : (List<AdvancedNodeSlotController>)newValue) {
                    inputContainer.getChildren().add(slot.getView());
                }
            }
        });

        selectedProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                if((Boolean)newValue)
                    getView().setBorder(BorderUtils.createHighlightBorder());
                else
                    getView().setBorder(null);
            }
        });

        outputSlotsProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                outputContainer.getChildren().clear();

                for(NodeSlotController slot : (List<NodeSlotController>)newValue) {
                    outputContainer.getChildren().add(slot.getView());
                }
            }
        });

        nodeClassProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                setNodeClassLabel((String)newValue);
            }
        });

        errorProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                String msg = (String)newValue;

                if(msg != null && msg.length() > 0) {
                    error.setText(msg);
                    errorPane.setVisible(true);
                    errorPane.setManaged(true);
                } else {
                    error.setText("");
                    errorPane.setVisible(false);
                    errorPane.setManaged(false);
                }
            }
        });

        xOffsetProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                getView().setLayoutX((Double)newValue);

                for(AdvancedNodeSlotController slot : getInputSlots()) {
                    for(ConnectionController connection : slot.getSlot().getConnections()) {
                        connection.invalidate();
                    }
                }

                for(NodeSlotController slot : getOutputSlots()) {
                    for(ConnectionController connection : slot.getConnections()) {
                        connection.invalidate();
                    }
                }
            }
        });

        yOffsetProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                getView().setLayoutY((Double)newValue);
            }
        });

        titleProperty.setBinder((Object node) -> {
            return ((Node)node).getProcessor().getName();
        });

        inputSlotsProperty.setBinder((Object node) -> {
            return BindingHelper.bindList(inputSlotsProperty, ((Node)node).getInputSlots(), (InputNodeSlot slot) -> new AdvancedNodeSlotController(NodeController.this, slot.getProperty().getType()));
        });

        outputSlotsProperty.setBinder((Object node) -> {
            return BindingHelper.bindList(outputSlotsProperty, ((Node)node).getOutputSlots(), (OutputNodeSlot slot) -> new NodeSlotController(this));
        });

        nodeClassProperty.setBinder((Object node) -> {
            Node n = (Node)node;
            if(n.getProcessor() instanceof SciProcessor) {
                return "SciLab";
            } else if(n.getProcessor() instanceof NativeProcessor) {
                return "C++";
            } else {
                return "Java";
            }
        });

        errorProperty.setBinder((Object node) -> {
            Node n = (Node)node;

            if(n.getLastError() == null)
                return "";
            else
                return n.getLastError().getMessage();
        });

        xOffsetProperty.setBinder((Object node) -> {
            return ((Node)node).getX();
        });

        yOffsetProperty.setBinder((Object node) -> {
            return ((Node)node).getY();
        });

        initialize();
    }

    public List<AdvancedNodeSlotController> getInputSlots() {
        return (List<AdvancedNodeSlotController>)inputSlotsProperty.getValue();
    }

    public List<NodeSlotController> getOutputSlots() {
        return (List<NodeSlotController>)outputSlotsProperty.getValue();
    }

    private void setNodeClassLabel(String label) {
        Text text = new Text(label);
        text.setLayoutY(title.getLayoutY());
        text.setLayoutX(nodeClass.getWidth() - 10.0);
        nodeClass.getChildren().clear();
        nodeClass.getChildren().add(text);
    }

    public void setSelected(boolean selected) {
        this.selectedProperty.setValue(selected);
    }

    public boolean isSelected() {
        return (Boolean)selectedProperty.getValue();
    }

    public void moveTo(double x, double y) {
        ((Node)getContext()).setLocation(x, y);
        poll(xOffsetProperty);
        poll(yOffsetProperty);
    }

    @FXML
    public void onMousePressed(MouseEvent event) {
        previousMouseX = event.getScreenX();
        previousMouseY = event.getScreenY();
        graphController.select(this, event.isControlDown());
        event.consume();
    }

    @FXML
    public void onMouseDragged(MouseEvent event) {
        double deltaX = event.getScreenX() - previousMouseX;
        double deltaY = event.getScreenY() - previousMouseY;
        previousMouseX = event.getScreenX();
        previousMouseY = event.getScreenY();
        getGraphController().moveSelected(deltaX, deltaY);
        event.consume();
    }

    @FXML
    public void onKeyPressed(KeyEvent event) {
        if(event.getCode() == KeyCode.DELETE) {
            getGraphController().removeSelected();
        }
    }

    public UIProperty errorProperty() {
        return errorProperty;
    }

    public GraphController getGraphController() {
        return graphController;
    }
}
