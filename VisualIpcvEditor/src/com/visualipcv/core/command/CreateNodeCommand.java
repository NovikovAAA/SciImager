package com.visualipcv.core.command;

import com.visualipcv.core.Graph;
import com.visualipcv.core.Node;

public class CreateNodeCommand extends GraphCommand {
    private Node node;

    public CreateNodeCommand(Graph graph, Node node) {
        super(graph);
        this.node = node;
    }

    @Override
    public void execute() {
        getGraph().addNode(node);
    }

    @Override
    public void rollback() {
        getGraph().removeNode(node);
    }

    public Node getNode() {
        return node;
    }
}
