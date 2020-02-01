package com.visualipcv.core.command;

import com.visualipcv.core.Graph;
import com.visualipcv.core.NodeSlot;

public class DisconnectCommand extends GraphCommand {
    private NodeSlot source;
    private NodeSlot target;

    public DisconnectCommand(Graph graph, NodeSlot source, NodeSlot target) {
        super(graph);
        this.source = source;
        this.target = target;
    }

    @Override
    public void execute() {
        target.disconnect();
    }

    @Override
    public void rollback() {
        target.connect(source);
    }
}
