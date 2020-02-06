package com.visualipcv.viewmodel;

import com.visualipcv.controller.GraphViewController;
import com.visualipcv.core.Connection;
import com.visualipcv.core.Graph;
import com.visualipcv.core.Node;
import com.visualipcv.core.Processor;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GraphViewModel extends ViewModel {

    public interface IGraphEventListener {
        void onAdded(Node node);
        void onRemoved(Node node);
        void onConnected();
        void onDisconnected();
    }

    private Graph graph = new Graph();

    private ObservableList<NodeViewModel> nodes = FXCollections.observableArrayList();
    private ObservableList<ConnectionViewModel> connections = FXCollections.observableArrayList();

    private List<IGraphEventListener> graphEventListeners = new ArrayList<>();

    public List<NodeViewModel> getNodes() {
        return nodes;
    }

    public Graph getGraph() {
        return graph;
    }

    public void addNode(Processor processor, double x, double y) {
        Node node = new Node(graph, processor, x, y);
        graph.addNode(node);
        onNodeAdded(node);
    }

    public void removeNode(NodeViewModel node) {
        graph.removeNode(node.getNode());
        onNodeRemoved(node.getNode());
    }

    public void selectNode(NodeViewModel node) {
        node.getIsSelected().set(true);
    }

    public void unselectNode(NodeViewModel node) {
        node.getIsSelected().set(false);
    }

    public void clearSelection() {
        for(NodeViewModel node : nodes) {
            node.getIsSelected().set(false);
        }
    }

    public void addListener(IGraphEventListener listener) {
        graphEventListeners.add(listener);
    }

    public void removeListener(IGraphEventListener listener) {
        graphEventListeners.remove(listener);
    }

    public void onNodeAdded(Node node) {
        for(IGraphEventListener listener : graphEventListeners)
            listener.onAdded(node);
    }

    public void onNodeRemoved(Node node) {
        for(IGraphEventListener listener : graphEventListeners)
            listener.onRemoved(node);
    }

    @Override
    public void update() {

    }
}
