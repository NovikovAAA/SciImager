package com.visualipcv.viewmodel;

import com.visualipcv.core.Connection;
import com.visualipcv.core.Graph;
import com.visualipcv.core.GraphExecutionException;
import com.visualipcv.core.Node;
import com.visualipcv.core.Processor;
import com.visualipcv.core.io.GraphStore;
import com.visualipcv.view.ConnectionView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class GraphViewModel extends ViewModel {

    public interface IGraphEventListener {
        void onAdded(Node node);
        void onRemoved(Node node);
        void onConnected(Connection connection);
        void onDisconnected(Connection connection);
        void onRequestSort();
    }

    private Graph graph;

    private ObservableList<NodeViewModel> nodes = FXCollections.observableArrayList();
    private ObservableList<ConnectionViewModel> connections = FXCollections.observableArrayList();
    private DoubleProperty zoom = new SimpleDoubleProperty(1.0);
    private DoubleProperty xOffset = new SimpleDoubleProperty(0.0);
    private DoubleProperty yOffset = new SimpleDoubleProperty(0.0);

    public GraphViewModel() {
        this.graph = new Graph();
        init();
    }

    public GraphViewModel(Graph graph) {
        this.graph = graph;
        init();
    }

    private void init() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.2), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for(NodeViewModel viewModel : nodes) {
                    viewModel.getErrorProperty().set("");
                }

                try {
                    graph.execute();
                } catch (GraphExecutionException e) {
                    for(NodeViewModel viewModel : nodes) {
                        if(viewModel.getNode() == e.getNode()) {
                            viewModel.getErrorProperty().set(e.getMessage());
                        }
                    }
                }
            }
        }));
        timeline.setCycleCount(-1);
        timeline.play();
    }

    private List<IGraphEventListener> graphEventListeners = new ArrayList<>();

    public List<NodeViewModel> getNodes() {
        return nodes;
    }

    public List<ConnectionViewModel> getConnections() {
        return connections;
    }

    public DoubleProperty getZoomProperty() {
        return zoom;
    }

    public DoubleProperty getXOffsetProperty() {
        return xOffset;
    }

    public DoubleProperty getYOffsetProperty() {
        return yOffset;
    }

    public List<NodeViewModel> getSelectedNodes() {
        List<NodeViewModel> selected = new ArrayList<>();
        for(NodeViewModel node : nodes) {
            if(node.getIsSelected().get()) {
                selected.add(node);
            }
        }
        return selected;
    }

    public void zoom(double mouseX, double mouseY, double delta) {
        double value = zoom.get() + delta;
        value = Math.min(20.0, Math.max(value, 0.1));
        double deltaScale = value - zoom.get();
        double deltaX = -mouseX * deltaScale;
        double deltaY = -mouseY * deltaScale;

        zoom.set(value);
        xOffset.set(xOffset.get() + deltaX);
        yOffset.set(yOffset.get() + deltaY);
    }

    public void setZoom(double value) {
        zoom.set(value);
    }

    public void setOffset(double x, double y) {
        xOffset.set(x);
        yOffset.set(y);
    }

    public void move(double deltaX, double deltaY) {
        xOffset.setValue(xOffset.getValue() + deltaX);
        yOffset.setValue(yOffset.getValue() + deltaY);
    }

    public void addNode(Processor processor, double x, double y) {
        Node node = new Node(graph, processor, x, y);
        graph.addNode(node);
        onNodeAdded(node);
    }

    public void removeNode(NodeViewModel node) {
        graph.removeNode(node.getNode());
        onNodeRemoved(node.getNode());
        update();
    }

    public void selectNode(NodeViewModel node) {
        node.getIsSelected().set(true);
        onRequestSort();
    }

    public void unselectNode(NodeViewModel node) {
        node.getIsSelected().set(false);
    }

    public void clearSelection() {
        for(NodeViewModel node : nodes) {
            unselectNode(node);
        }
    }

    public void moveSelected(double deltaX, double deltaY) {
        for(NodeViewModel node : getSelectedNodes()) {
            node.getLayoutXProperty().set(node.getLayoutXProperty().get() + deltaX / zoom.get());
            node.getLayoutYProperty().set(node.getLayoutYProperty().get() + deltaY / zoom.get());
        }
    }

    public void removeSelected() {
        for(NodeViewModel node : getSelectedNodes()) {
            removeNode(node);
        }
    }

    public void addListener(IGraphEventListener listener) {
        graphEventListeners.add(listener);
    }

    public void removeListener(IGraphEventListener listener) {
        graphEventListeners.remove(listener);
    }

    @Override
    public void update() {
        for(Node node : graph.getNodes()) {
            if(nodes.stream().noneMatch((NodeViewModel vm) -> vm.getNode() == node)) {
                onNodeAdded(node);
            }
        }

        for(NodeViewModel viewModel : nodes) {
            if(graph.getNodes().stream().noneMatch((Node n) -> n == viewModel.getNode())) {
                onNodeRemoved(viewModel.getNode());
            }
        }

        for(NodeViewModel viewModel : nodes)
            viewModel.update();

        {
            Set<Integer> connectionViewModelHash = new HashSet<>();

            for(ConnectionViewModel viewModel : connections) {
                connectionViewModelHash.add(Objects.hash(viewModel.getSource().getNodeSlot(), viewModel.getTarget().getNodeSlot()));
            }

            for(Connection connection : graph.getConnections()) {
                if(!connectionViewModelHash.contains(Objects.hash(connection.getSource(), connection.getTarget()))) {
                    onConnected(connection);
                }
            }
        }

        {
            Set<Integer> connectionHash = new HashSet<>();

            for(Connection connection : graph.getConnections()) {
                connectionHash.add(Objects.hash(connection.getSource(), connection.getTarget()));
            }

            List<ConnectionViewModel> connectionsToRemove = new ArrayList<>();

            for(ConnectionViewModel viewModel : connections) {
                if(!connectionHash.contains(Objects.hash(viewModel.getSource().getNodeSlot(), viewModel.getTarget().getNodeSlot()))) {
                    connectionsToRemove.add(viewModel);
                }
            }

            for(ConnectionViewModel viewModel : connectionsToRemove) {
                onDisconnected(new Connection(viewModel.getSource().getNodeSlot(), viewModel.getTarget().getNodeSlot()));
            }
        }

        for(ConnectionViewModel viewModel : connections) {
            viewModel.update();
        }
    }

    public void save(String path) throws Exception {
        new GraphStore().save(graph, new FileOutputStream(path));
    }

    public void load(String path) throws Exception {
        graph = new GraphStore().load(new FileInputStream(path));
        update();
    }

    private void onNodeAdded(Node node) {
        for(IGraphEventListener listener : graphEventListeners)
            listener.onAdded(node);
    }

    private void onNodeRemoved(Node node) {
        for(IGraphEventListener listener : graphEventListeners)
            listener.onRemoved(node);
    }

    private void onConnected(Connection connection) {
        for(IGraphEventListener listener : graphEventListeners)
            listener.onConnected(connection);
    }

    private void onDisconnected(Connection connection) {
        for(IGraphEventListener listener : graphEventListeners)
            listener.onDisconnected(connection);
    }

    private void onRequestSort() {
        for(IGraphEventListener listener : graphEventListeners)
            listener.onRequestSort();
    }
}
