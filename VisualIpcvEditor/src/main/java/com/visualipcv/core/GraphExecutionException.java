package com.visualipcv.core;

public class GraphExecutionException extends Exception {
    private Node node;

    public GraphExecutionException(Node node, String message) {
        super(message);
        this.node = node;
    }

    public Node getNode() {
        return node;
    }
}
