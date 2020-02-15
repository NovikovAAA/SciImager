package com.visualipcv.viewmodel;

import com.visualipcv.core.Node;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;

public class NodeViewModel extends ViewModel {
    private Node node;
    private GraphViewModel graph;

    private ObjectProperty<Point2D> position = new ObjectPropertyBase<Point2D>() {
        @Override
        public Object getBean() {
            return NodeViewModel.this;
        }

        @Override
        public String getName() {
            return "position";
        }

        @Override
        public void invalidated() {
            node.setLocation(position.get().getX(), position.get().getY());
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
        update();
    }

    public GraphViewModel getGraph() {
        return graph;
    }

    public Node getNode() {
        return node;
    }

    public ObjectProperty<Point2D> getPositionProperty() {
        return position;
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
        position.set(new Point2D(node.getX(), node.getY()));
        title.set(node.getProcessor().getName());

        for(NodeSlotViewModel viewModel : getInputNodeSlots()) {
            viewModel.update();
        }

        for(NodeSlotViewModel viewModel : getOutputNodeSlots()) {
            viewModel.update();
        }
    }
}
