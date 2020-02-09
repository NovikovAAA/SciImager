package com.visualipcv.core.command;

import com.visualipcv.core.Graph;

public abstract class GraphCommand extends Command {
    private Graph graph;

    public GraphCommand(Graph graph) {
        this.graph = graph;
    }

    public Graph getGraph() {
        return graph;
    }
}
