package com.visualipcv.controller;

import com.visualipcv.controller.binding.BindingHelper;
import com.visualipcv.controller.binding.PropertyChangedEventListener;
import com.visualipcv.controller.binding.UIProperty;
import com.visualipcv.core.InputNodeSlot;
import com.visualipcv.core.NativeProcessor;
import com.visualipcv.core.Node;
import com.visualipcv.core.NodeCommand;
import com.visualipcv.core.NodeSlot;
import com.visualipcv.core.OutputNodeSlot;
import com.visualipcv.core.ProcessorCommand;
import com.visualipcv.core.SciProcessor;
import com.visualipcv.editor.Editor;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.Mnemonic;
import javafx.scene.input.MouseButton;
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
    @FXML
    private AnchorPane wrapper;

    private UIProperty selectedProperty = new UIProperty(false);
    private UIProperty titleProperty = new UIProperty();
    private UIProperty inputSlotsProperty = new UIProperty();
    private UIProperty outputSlotsProperty = new UIProperty();
    private UIProperty nodeClassProperty = new UIProperty();
    private UIProperty errorProperty = new UIProperty();
    private UIProperty xOffsetProperty = new UIProperty();
    private UIProperty yOffsetProperty = new UIProperty();

    private ContextMenu contextMenu;

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
                if((Boolean)newValue) {
                    wrapper.setBorder(BorderUtils.createHighlightBorder());
                    AnchorPane.setBottomAnchor(wrapper, 0.0);
                    AnchorPane.setTopAnchor(wrapper, 0.0);
                    AnchorPane.setLeftAnchor(wrapper, 0.0);
                    AnchorPane.setRightAnchor(wrapper, 0.0);
                }
                else {
                    wrapper.setBorder(null);
                    AnchorPane.setBottomAnchor(wrapper, 3.0);
                    AnchorPane.setTopAnchor(wrapper, 3.0);
                    AnchorPane.setLeftAnchor(wrapper, 3.0);
                    AnchorPane.setRightAnchor(wrapper, 3.0);
                }
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

        getView().addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                contextMenu.hide();
            }
        });

        initialize();
    }

    private void addDefaultCommands() {
        Node node = (Node)getContext();

        node.addCommand(new NodeCommand() {
            @Override
            public void execute(Node node) {
                getGraphController().copy();
            }

            @Override
            public String getName() {
                return "Copy";
            }
        });

        node.addCommand(new NodeCommand() {
            @Override
            public void execute(Node node) {
                getGraphController().cut();
            }

            @Override
            public String getName() {
                return "Cut";
            }
        });
    }

    private ContextMenu createContextMenu() {
        ContextMenu menu = new ContextMenu();

        for(NodeCommand command : ((Node)getContext()).getCommands()) {
            MenuItem item = new MenuItem(command.getName());
            item.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    command.execute(((Node)getContext()));
                }
            });
            menu.getItems().add(item);
        }

        for(ProcessorCommand command : ((Node)getContext()).getProcessor().getCommands()) {
            MenuItem item = new MenuItem(command.getName());
            item.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    command.execute(((Node)getContext()).getState());
                }
            });
            menu.getItems().add(item);
        }

        return menu;
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
    public void onMouseReleased(MouseEvent event) {
        if(event.getButton() == MouseButton.SECONDARY) {
            contextMenu.show(getView(), event.getScreenX(), event.getScreenY());
            event.consume();
        }
    }

    @FXML
    public void onMouseDragged(MouseEvent event) {
        double deltaX = event.getScreenX() - previousMouseX;
        double deltaY = event.getScreenY() - previousMouseY;
        previousMouseX = event.getScreenX();
        previousMouseY = event.getScreenY();

        if(event.getButton() == MouseButton.PRIMARY) {
            getGraphController().moveSelected(deltaX, deltaY);
            event.consume();
        }
    }

    @FXML
    public void onKeyPressed(KeyEvent event) {
        if(event.getCode() == KeyCode.DELETE) {
            getGraphController().removeSelected();
            event.consume();
        }
    }

    public UIProperty errorProperty() {
        return errorProperty;
    }

    public GraphController getGraphController() {
        return graphController;
    }

    @Override
    public void setContext(Object context) {
        super.setContext(context);
        addDefaultCommands();
        contextMenu = createContextMenu();
    }
}
