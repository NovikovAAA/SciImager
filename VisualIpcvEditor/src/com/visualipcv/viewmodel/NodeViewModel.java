package com.visualipcv.viewmodel;

import com.visualipcv.core.InputNodeSlot;
import com.visualipcv.core.Node;
import com.visualipcv.core.NodeSlot;
import com.visualipcv.core.Processor;
import com.visualipcv.view.NodeSlotView;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class NodeViewModel extends ViewModel {
    private Node node;
    private GraphViewModel graph;

    private DoubleProperty layoutX = new DoublePropertyBase() {
        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "layoutX";
        }

        @Override
        public void invalidated() {
            node.setLocation(layoutX.getValue(), node.getY());
        }
    };

    private DoubleProperty layoutY = new DoublePropertyBase() {
        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "layoutY";
        }

        @Override
        public void invalidated() {
            node.setLocation(node.getX(), layoutY.getValue());
        }
    };

    private BooleanProperty isSelected = new SimpleBooleanProperty(false);

    private StringProperty title = new SimpleStringProperty();
    private StringProperty error = new SimpleStringProperty("");
    private ObservableList<NodeSlotViewModel> inputNodeSlots = FXCollections.observableArrayList();
    private ObservableList<NodeSlotViewModel> outputNodeSlots = FXCollections.observableArrayList();

    public NodeViewModel(GraphViewModel graph, Node node) {
        this.node = node;
        this.graph = graph;
        title.setValue(node.getProcessor().getName());
        update();
    }

    public GraphViewModel getGraph() {
        return graph;
    }

    public Node getNode() {
        return node;
    }

    public DoubleProperty getLayoutXProperty() {
        return layoutX;
    }

    public DoubleProperty getLayoutYProperty() {
        return layoutY;
    }

    public StringProperty getTitleProperty() {
        return title;
    }

    public StringProperty getErrorProperty() {
        return error;
    }

    public BooleanProperty getIsSelected() {
        return isSelected;
    }

    public ObservableList<NodeSlotViewModel> getInputNodeSlots() {
        return inputNodeSlots;
    }

    public ObservableList<NodeSlotViewModel> getOutputNodeSlots() {
        return outputNodeSlots;
    }

    public void removeSelected() {
        graph.removeSelected();
    }

    public void moveSelected(double deltaX, double deltaY) {
        graph.moveSelected(deltaX, deltaY);
    }

    public void selectNode(boolean ctrlPressed) {
        if(!isSelected.get() && !ctrlPressed)
            graph.clearSelection();
        graph.selectNode(this);
    }

    @Override
    public void update() {
        layoutX.set(node.getX());
        layoutY.set(node.getY());
    }
}
