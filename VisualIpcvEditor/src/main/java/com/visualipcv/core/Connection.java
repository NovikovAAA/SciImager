package com.visualipcv.core;

import com.visualipcv.core.io.ConnectionEntity;

import java.util.Objects;

public class Connection {
    private NodeSlot source;
    private NodeSlot target;

    public Connection(NodeSlot source, NodeSlot target) {
        this.source = source;
        this.target = target;
    }

    public Connection(Graph graph, ConnectionEntity connectionEntity) {
        Node sourceNode = graph.findNode(connectionEntity.getSourceNodeId());
        Node targetNode = graph.findNode(connectionEntity.getTargetNodeId());
        source = sourceNode.getNodeSlot(connectionEntity.getSourceNodeProperty());
        target = targetNode.getNodeSlot(connectionEntity.getTargetNodeProperty());
    }

    public NodeSlot getSource() {
        return source;
    }

    public NodeSlot getTarget() {
        return target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Connection that = (Connection) o;
        return Objects.equals(source, that.source) &&
                Objects.equals(target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target);
    }
}
