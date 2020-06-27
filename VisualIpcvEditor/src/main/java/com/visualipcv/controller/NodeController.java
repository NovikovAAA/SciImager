package com.visualipcv.controller;

import com.visualipcv.controller.binding.BindingHelper;
import com.visualipcv.controller.binding.FactoryFunction;
import com.visualipcv.controller.binding.PropertyChangedEventListener;
import com.visualipcv.controller.binding.UIProperty;
import com.visualipcv.core.GraphElement;
import com.visualipcv.core.InputNodeSlot;
import com.visualipcv.core.NativeProcessor;
import com.visualipcv.core.Node;
import com.visualipcv.core.NodeCommand;
import com.visualipcv.core.NodeSlot;
import com.visualipcv.core.OutputNodeSlot;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorCommand;
import com.visualipcv.core.ProcessorLibrary;
import com.visualipcv.core.SciProcessor;
import com.visualipcv.editor.Editor;
import com.visualipcv.view.EditableLabel;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import javax.tools.Tool;
import java.util.ArrayList;
import java.util.List;

public class NodeController extends GraphElementController<AnchorPane> {
    private GraphController graphController;

    @FXML
    private VBox inputContainer;
    @FXML
    private VBox outputContainer;
    @FXML
    private StackPane content;
    @FXML
    private Text error;
    @FXML
    private Pane errorPane;
    @FXML
    private StackPane nodeClass;

    private final UIProperty inputSlotsProperty = new UIProperty();
    private final UIProperty outputSlotsProperty = new UIProperty();
    private final UIProperty nodeClassProperty = new UIProperty();
    private final UIProperty errorProperty = new UIProperty();
    private final UIProperty isProxyProperty = new UIProperty();

    private ContextMenu contextMenu;
    private Tooltip errorTooltip;

    public NodeController(GraphController controller) {
        super(controller, AnchorPane.class, "NodeView.fxml");
        this.graphController = controller;

        isProxyProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                getView().pseudoClassStateChanged(PseudoClass.getPseudoClass("proxy"), (Boolean)newValue);
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

        outputSlotsProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                outputContainer.getChildren().clear();

                for(AdvancedNodeSlotController slot : (List<AdvancedNodeSlotController>)newValue) {
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
                    error.setText("Error occurred (see more)");
                    errorPane.setVisible(true);
                    errorPane.setManaged(true);
                    errorTooltip = new Tooltip(msg);
                    errorTooltip.setAutoHide(true);
                    Tooltip.install(errorPane, errorTooltip);
                } else {
                    error.setText("");
                    errorPane.setVisible(false);
                    errorPane.setManaged(false);
                    error.onMouseClickedProperty().unbind();
                }
            }
        });

        xOffsetProperty().addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                getView().setLayoutX((Double)newValue);

                for(AdvancedNodeSlotController slot : getInputSlots()) {
                    for(ConnectionController connection : slot.getSlot().getConnections()) {
                        connection.invalidate();
                    }
                }

                for(AdvancedNodeSlotController slot : getOutputSlots()) {
                    for(ConnectionController connection : slot.getSlot().getConnections()) {
                        connection.invalidate();
                    }
                }
            }
        });

        yOffsetProperty().addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                getView().setLayoutY((Double)newValue);
            }
        });

        isProxyProperty.setBinder((Object node) -> {
            return ((Node)node).isProxy();
        });

        inputSlotsProperty.setBinder((Object node) -> {
            return BindingHelper.bindList(inputSlotsProperty, ((Node)node).getInputSlots(), (InputNodeSlot slot) -> new AdvancedNodeSlotController(NodeController.this, slot.getProperty().getType()));
        });

        outputSlotsProperty.setBinder((Object node) -> {
            return BindingHelper.bindList(outputSlotsProperty, ((Node) node).getOutputSlots(), (OutputNodeSlot slot) -> new AdvancedNodeSlotController(NodeController.this, slot.getProperty().getType()));
        });

        nodeClassProperty.setBinder((Object node) -> {
            Node n = (Node)node;

            if(n.isProxy())
                return "";

            if(n.findProcessor() instanceof SciProcessor) {
                return "SciLab";
            } else if(n.findProcessor() instanceof NativeProcessor) {
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

        getView().addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                NodeController.this.contextMenu.hide();
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

        node.addCommand(new NodeCommand() {
            @Override
            public void execute(Node node) {
                getGraphController().removeSelected();
            }

            @Override
            public String getName() {
                return "Delete";
            }
        });

        node.addCommand(new NodeCommand() {
            @Override
            public void execute(Node node) {
                getGraphController().groupSelected();
            }

            @Override
            public String getName() {
                return "Create group";
            }
        });
    }

    private ContextMenu createContextMenu() {
        ContextMenu menu = new ContextMenu();
        Node node = (Node)getContext();

        if(node.isProxy()) {
            MenuItem refresh = new MenuItem("Refresh");
            refresh.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Processor processor = node.findProcessor();

                    if(processor == null)
                        return;

                    node.getGraph().replaceWithUpdatedProcessor(node, processor);
                    getGraphController().invalidate();
                }
            });

            menu.getItems().add(refresh);
        }

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

        MenuItem descriptionItem = new MenuItem("Add description");
        descriptionItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ((GraphElement)getContext()).setDescription("Just a node");
                invalidate();
            }
        });

        menu.getItems().add(descriptionItem);

        if(!node.isProxy()) {
            for(ProcessorCommand command : ((Node)getContext()).findProcessor().getCommands()) {
                MenuItem item = new MenuItem(command.getName());
                item.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        command.execute(getGraphController().getExecutionContext().load(node));
                    }
                });
                menu.getItems().add(item);
            }
        }

        return menu;
    }

    public List<AdvancedNodeSlotController> getInputSlots() {
        return (List<AdvancedNodeSlotController>)inputSlotsProperty.getValue();
    }

    public List<AdvancedNodeSlotController> getOutputSlots() {
        return (List<AdvancedNodeSlotController>)outputSlotsProperty.getValue();
    }

    private void setNodeClassLabel(String label) {
        Text text = new Text(label);
        nodeClass.getChildren().clear();
        nodeClass.getChildren().add(text);
    }

    @FXML
    public void onMouseReleased(MouseEvent event) {
        if(event.getButton() == MouseButton.SECONDARY) {
            contextMenu.show(getView(), event.getScreenX(), event.getScreenY());
            event.consume();
        }
    }

    public UIProperty errorProperty() {
        return errorProperty;
    }

    @Override
    public void setContext(Object context) {
        super.setContext(context);

        Node node = (Node)context;
        node.getCommands().clear();
        addDefaultCommands();

        if(node.findProcessor() != null) {
            if(node.findProcessor().isProperty()) {
                getTitle().setEditable(true);
                getView().pseudoClassStateChanged(PseudoClass.getPseudoClass("property"), true);
            }
            else {
                getTitle().setEditable(false);
                getView().pseudoClassStateChanged(PseudoClass.getPseudoClass("property"),  false);
            }
        }

        invalidate();
    }

    public void setContent(Image image) {
        if(image == null) {
            content.getChildren().clear();
            return;
        }

        ImageView view = new ImageView(image);
        double scale = image.getWidth() / (content.getMaxWidth() * 0.9);

        view.setFitWidth(image.getWidth() / scale);
        view.setFitHeight(image.getHeight() / scale);
        content.setPrefHeight(view.getFitHeight() / 0.9);
        content.setPrefWidth(view.getFitWidth() / 0.9);
        content.getChildren().clear();
        content.getChildren().add(view);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        contextMenu = createContextMenu();
        getView().requestLayout();
    }
}
