package com.visualipcv.viewmodel;

import com.visualipcv.core.InputNodeSlot;
import com.visualipcv.core.Node;
import com.visualipcv.core.NodeSlot;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class NodeViewModel {
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
            node.setLocation(layoutX.getValue(), layoutY.getValue());
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
            node.setLocation(layoutX.getValue(), layoutY.getValue());
        }
    };

    private StringProperty title = new SimpleStringProperty();
    private ObservableList<InputNodeSlot> inputNodeSlots = FXCollections.observableArrayList();
    private ObservableList<NodeSlot> outputNodeSlots = FXCollections.observableArrayList();

    public NodeViewModel(GraphViewModel graph, Node node) {
        this.node = node;
        this.graph = graph;
        title.setValue(node.getProcessor().getName());

        double x = node.getX();
        double y = node.getY();
        layoutX.setValue(x);
        layoutY.setValue(y);
    }

    public void init() {
        for(InputNodeSlot slot : node.getInputSlots()) {
            if(slot.getProperty().showConnector()) {
                inputNodeSlots.add(slot);
            }
        }
        outputNodeSlots.addAll(node.getOutputSlots());
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

    public ObservableList<InputNodeSlot> getInputNodeSlots() {
        return inputNodeSlots;
    }

    public ObservableList<NodeSlot> getOutputNodeSlots() {
        return outputNodeSlots;
    }
}
