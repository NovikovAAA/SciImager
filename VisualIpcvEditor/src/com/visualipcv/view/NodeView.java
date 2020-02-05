package com.visualipcv.view;

import com.visualipcv.controller.GraphViewController;
import com.visualipcv.controller.IGraphViewElement;
import com.visualipcv.core.Graph;
import com.visualipcv.core.InputNodeSlot;
import com.visualipcv.core.Node;
import com.visualipcv.core.NodeSlot;
import com.visualipcv.core.OutputNodeSlot;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorProperty;
import com.visualipcv.viewmodel.NodeViewModel;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.DoubleProperty;
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
import jdk.internal.util.xml.impl.Input;

import java.io.IOException;
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

    private ObservableList<NodeSlotView> inputSlots = FXCollections.observableArrayList();
    private ObservableList<NodeSlotView> outputSlots = FXCollections.observableArrayList();

    public NodeView(GraphView graphView, Processor processor, double x, double y) {
        viewModel = new NodeViewModel(graphView.getViewModel(), processor);
        this.graphView = graphView;

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
        selected.set(false);

        setLayoutX(x);
        setLayoutY(y);

        inputSlots.addListener(new ListChangeListener<NodeSlotView>() {
            @Override
            public void onChanged(Change<? extends NodeSlotView> c) {
                while(c.next()) {
                    if(c.wasAdded()) {
                        for(NodeSlotView slotView : c.getAddedSubList()) {
                            viewModel.getInputNodeSlots().add(slotView.getViewModel());
                            inputContainer.getChildren().add(slotView);
                        }
                    }
                    if(c.wasRemoved()) {
                        for(NodeSlotView slotView : c.getRemoved()) {
                            viewModel.getInputNodeSlots().remove(slotView.getViewModel());
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

    public NodeSlotView findSlotViewByModel(NodeSlot slot) {
        for(NodeSlotView view : inputSlots) {
            if(view.getViewModel().getNodeSlot() == slot) {
                return view;
            }
        }
        for(NodeSlotView view : outputSlots) {
            if(view.getViewModel().getNodeSlot() == slot) {
                return view;
            }
        }
        return null;
    }

    public List<NodeSlotView> getInputSlots() {
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

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    @FXML
    public void onMousePressed(MouseEvent event) {
        previousMouseX = event.getScreenX();
        previousMouseY = event.getScreenY();
        graphView.selectNode(this, event.isControlDown());
        requestFocus();
        event.consume();
    }

    @FXML
    public void onMouseDragged(MouseEvent event) {
        double deltaX = event.getScreenX() - previousMouseX;
        double deltaY = event.getScreenY() - previousMouseY;
        previousMouseX = event.getScreenX();
        previousMouseY = event.getScreenY();
        graphView.moveSelectedNodes(deltaX, deltaY);
        event.consume();
    }

    @FXML
    public void onKeyPressed(KeyEvent event) {
        if(event.getCode() == KeyCode.DELETE) {
            graphView.removeSelectedNodes();
        }
    }

    @Override
    public GraphViewController getController() {
        return getGraphView().getController();
    }

    @Override
    public GraphView getGraphView() {
        return graphView;
    }
}
