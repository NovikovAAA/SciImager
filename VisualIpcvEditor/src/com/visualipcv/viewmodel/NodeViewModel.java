package com.visualipcv.viewmodel;

import com.visualipcv.core.InputNodeSlot;
import com.visualipcv.core.Node;
import com.visualipcv.core.NodeSlot;
import com.visualipcv.core.Processor;
import com.visualipcv.view.NodeSlotView;
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
    private ObservableList<NodeSlotViewModel> inputNodeSlots = FXCollections.observableArrayList();
    private ObservableList<NodeSlotViewModel> outputNodeSlots = FXCollections.observableArrayList();

    public NodeViewModel(GraphViewModel graph, Processor processor) {
        this.node = new Node(graph.getGraph(), processor, layoutX.get(), layoutY.get());
        this.graph = graph;
        title.setValue(node.getProcessor().getName());
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

    public ObservableList<NodeSlotViewModel> getInputNodeSlots() {
        return inputNodeSlots;
    }

    public ObservableList<NodeSlotViewModel> getOutputNodeSlots() {
        return outputNodeSlots;
    }
}
