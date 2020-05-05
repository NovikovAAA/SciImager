package com.visualipcv.core;

import com.visualipcv.core.io.ConnectionEntity;

import java.util.Objects;

public class Connection {
    private NodeSlot source;
    private NodeSlot target;

    public Connection(NodeSlot source, NodeSlot target) {
        this.source = source;
        this.target = target;
        validate();
    }

    public Connection(Graph graph, ConnectionEntity connectionEntity) {
        Node sourceNode = graph.findNode(connectionEntity.getSourceNodeId());
        Node targetNode = graph.findNode(connectionEntity.getTargetNodeId());

        if(sourceNode == null || targetNode == null) {
            throw new CommonException("Cannot find nodes for connection");
        }

        source = sourceNode.getNodeSlot(connectionEntity.getSourceNodeProperty());
        target = targetNode.getNodeSlot(connectionEntity.getTargetNodeProperty());
        validate();
    }

    private void validate() {
        if(source == null || target == null) {
            throw new CommonException("Cannot find slots for connection, probably connected nodes have been changed");
        }

        if(source.getClass() == target.getClass()) {
            throw new CommonException("Both slots of connection are " + source.getClass().getName());
        }
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
