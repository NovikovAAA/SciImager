package com.visualipcv.core.command;

import com.visualipcv.core.Graph;
import com.visualipcv.core.Node;

public class RemoveNodeCommand extends GraphCommand {
    private Node node;

    public RemoveNodeCommand(Graph graph, Node node) {
        super(graph);
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    @Override
    public void execute() {
        getGraph().removeNode(node);
    }

    @Override
    public void rollback() {
        getGraph().addNode(node);
    }
}
