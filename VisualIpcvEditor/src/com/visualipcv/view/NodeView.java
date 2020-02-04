package com.visualipcv.view;

import com.visualipcv.core.Graph;
import com.visualipcv.core.InputNodeSlot;
import com.visualipcv.core.Node;
import com.visualipcv.core.NodeSlot;
import com.visualipcv.viewmodel.NodeViewModel;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

public class NodeView extends AnchorPane {
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

        layoutXProperty().bindBidirectional(viewModel.getLayoutXProperty());
        layoutYProperty().bindBidirectional(viewModel.getLayoutYProperty());
        title.textProperty().bind(viewModel.getTitleProperty());
        selected.set(false);

        viewModel.getInputNodeSlots().addListener(new ListChangeListener<InputNodeSlot>() {
            @Override
            public void onChanged(Change<? extends InputNodeSlot> c) {
                while(c.next()) {
                    if(c.wasAdded()) {
                        for(NodeSlot slot : c.getAddedSubList()) {
                            inputSlots.add(new NodeSlotView(NodeView.this, slot));
                        }
                    }
                }
            }
        });

        viewModel.getOutputNodeSlots().addListener(new ListChangeListener<NodeSlot>() {
            @Override
            public void onChanged(Change<? extends NodeSlot> c) {
                while(c.next()) {
                    if(c.wasAdded()) {
                        for(NodeSlot slot : c.getAddedSubList()) {
                            outputSlots.add(new NodeSlotView(NodeView.this, slot));
                        }
                    }
                }
            }
        });

        inputSlots.addListener(new ListChangeListener<NodeSlotView>() {
            @Override
            public void onChanged(Change<? extends NodeSlotView> c) {
                while(c.next()) {
                    if(c.wasAdded()) {
                        for(NodeSlotView view : c.getAddedSubList()) {
                            inputContainer.getChildren().add(view);
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
                        for(NodeSlotView view : c.getAddedSubList()) {
                            outputContainer.getChildren().add(view);
                        }
                    }
                }
            }
        });

        viewModel.init();
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
}
