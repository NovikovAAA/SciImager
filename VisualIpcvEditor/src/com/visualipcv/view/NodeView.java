package com.visualipcv.view;

import com.visualipcv.controller.IGraphViewElement;
import com.visualipcv.core.NativeProcessor;
import com.visualipcv.core.Node;
import com.visualipcv.core.NodeSlot;
import com.visualipcv.viewmodel.NodeViewModel;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

import java.io.IOException;
import java.lang.annotation.Native;
import java.util.List;

public class NodeView extends AnchorPane implements IGraphViewElement {
    private NodeViewModel viewModel;
    private GraphView graphView;

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

    private BooleanProperty selected = new BooleanPropertyBase() {
        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "selected";
        }

        @Override
        public void invalidated() {
            if (selected.get()) {
                setBorder(new Border(new BorderStroke(Color.ORANGE, BorderStrokeStyle.SOLID, new CornerRadii(10.0), new BorderWidths(3.0))));
            } else {
                setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(10.0), new BorderWidths(1.0))));
            }
        }
    };

    private ObservableList<AdvancedNodeSlotView> inputSlots = FXCollections.observableArrayList();
    private ObservableList<NodeSlotView> outputSlots = FXCollections.observableArrayList();

    public NodeView(GraphView graphView, Node node) {
        viewModel = new NodeViewModel(graphView.getViewModel(), node);
        this.graphView = graphView;

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("NodeView.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch(IOException e) {
            e.printStackTrace();
        }

        if(!(node.getProcessor() instanceof NativeProcessor)) {
            Text text = new Text("Java");
            text.setLayoutY(title.getLayoutY());
            text.setLayoutX(nodeClass.getWidth() - 10.0);
            nodeClass.getChildren().add(text);
        }

        layoutXProperty().bindBidirectional(viewModel.getLayoutXProperty());
        layoutYProperty().bindBidirectional(viewModel.getLayoutYProperty());
        title.textProperty().bind(viewModel.getTitleProperty());
        error.textProperty().bind(viewModel.getErrorProperty());
        errorPane.visibleProperty().bind(viewModel.getErrorProperty().isNotEmpty());
        errorPane.managedProperty().bind(viewModel.getErrorProperty().isNotEmpty());
        selected.bind(viewModel.getIsSelected());

        for(NodeSlot slot : getViewModel().getNode().getInputSlots()) {
            AdvancedNodeSlotView slotView = new AdvancedNodeSlotView(this, slot, false);
            inputSlots.add(slotView);
            inputContainer.getChildren().add(slotView);

            if(slotView.getSlotView() != null)
                viewModel.getInputNodeSlots().add(slotView.getSlotView().getViewModel());
        }

        for(NodeSlot slot : getViewModel().getNode().getOutputSlots()) {
            if(!slot.getProperty().showConnector())
                continue;
            NodeSlotView slotView = new NodeSlotView(this, slot);
            outputSlots.add(slotView);
            viewModel.getOutputNodeSlots().add(slotView.getViewModel());
            outputContainer.getChildren().add(slotView);
        }

        inputSlots.addListener(new ListChangeListener<AdvancedNodeSlotView>() {
            @Override
            public void onChanged(Change<? extends AdvancedNodeSlotView> c) {
                while(c.next()) {
                    if(c.wasAdded()) {
                        for(AdvancedNodeSlotView slotView : c.getAddedSubList()) {
                            viewModel.getInputNodeSlots().add(slotView.getSlotView().getViewModel());
                            inputContainer.getChildren().add(slotView);
                        }
                    }
                    if(c.wasRemoved()) {
                        for(AdvancedNodeSlotView slotView : c.getRemoved()) {
                            viewModel.getInputNodeSlots().remove(slotView.getSlotView().getViewModel());
                            inputContainer.getChildren().remove(slotView);
                        }
                    }
                }
            }
        });

        outputSlots.addListener(new ListChangeListener<NodeSlotView>() {
            @Override
            public void onChanged(Change<? extends NodeSlotView> c) {
                while(c.next()) {
                    if(c.wasAdded()) {
                        for(NodeSlotView slotView : c.getAddedSubList()) {
                            viewModel.getOutputNodeSlots().add(slotView.getViewModel());
                            outputContainer.getChildren().add(slotView);
                        }
                    }
                    if(c.wasRemoved()) {
                        for(NodeSlotView slotView : c.getRemoved()) {
                            viewModel.getOutputNodeSlots().remove(slotView.getViewModel());
                            outputContainer.getChildren().remove(slotView);
                        }
                    }
                }
            }
        });
    }

    public List<AdvancedNodeSlotView> getInputSlots() {
        return inputSlots;
    }

    public List<NodeSlotView> getOutputSlots() {
        return outputSlots;
    }

    public NodeViewModel getViewModel() {
        return viewModel;
    }

    public BooleanProperty getSelectedProperty() {
        return selected;
    }

    public boolean isSelected() {
        return selected.get();
    }

    @FXML
    public void onMousePressed(MouseEvent event) {
        previousMouseX = event.getScreenX();
        previousMouseY = event.getScreenY();
        viewModel.selectNode(event.isControlDown());
        requestFocus();
        event.consume();
    }

    @FXML
    public void onMouseDragged(MouseEvent event) {
        double deltaX = event.getScreenX() - previousMouseX;
        double deltaY = event.getScreenY() - previousMouseY;
        previousMouseX = event.getScreenX();
        previousMouseY = event.getScreenY();
        viewModel.moveSelected(deltaX, deltaY);
        event.consume();
    }

    @FXML
    public void onKeyPressed(KeyEvent event) {
        if(event.getCode() == KeyCode.DELETE) {
            viewModel.removeSelected();
        }
    }

    @Override
    public GraphView getGraphView() {
        return graphView;
    }
}
