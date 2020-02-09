package com.visualipcv.core;

import com.visualipcv.core.command.Command;
import com.visualipcv.core.command.CommandQueue;

public class Document {
    private String name;
    private Graph graph = new Graph();
    private CommandQueue commandQueue = new CommandQueue();

    public Document(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Graph getGraph() {
        return graph;
    }

    public void execute(Command command) {
        command.execute();
        commandQueue.add(command);
    }

    public void undo() {
        commandQueue.undo();
    }

    public void redo() {
        commandQueue.redo();
    }
}
