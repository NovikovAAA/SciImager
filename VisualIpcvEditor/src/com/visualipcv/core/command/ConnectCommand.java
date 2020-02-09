package com.visualipcv.core.command;

import com.visualipcv.core.Graph;
import com.visualipcv.core.NodeSlot;

public class ConnectCommand extends GraphCommand {
    private NodeSlot source;
    private NodeSlot target;

    public ConnectCommand(Graph graph, NodeSlot source, NodeSlot target) {
        super(graph);
        this.source = source;
        this.target = target;
    }

    @Override
    public void execute() {
        target.connect(source);
    }

    @Override
    public void rollback() {
        target.disconnect();
    }
}
