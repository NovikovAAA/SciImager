package com.visualipcv.controller;

import com.visualipcv.NodeSlot;

import java.util.Objects;

public class ConnectionKey {
    private final NodeSlot sourceSlot;
    private final NodeSlot targetSlot;

    public ConnectionKey(NodeSlot sourceSlot, NodeSlot targetSlot) {
        this.sourceSlot = sourceSlot;
        this.targetSlot = targetSlot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConnectionKey that = (ConnectionKey) o;
        return sourceSlot == that.sourceSlot &&
                targetSlot == that.targetSlot;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceSlot, targetSlot);
    }
}
