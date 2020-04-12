package com.visualipcv.core;

public abstract class NodeCommand {
    public abstract void execute(Node node);
    public abstract String getName();
}
