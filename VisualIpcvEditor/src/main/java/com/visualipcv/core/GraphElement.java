package com.visualipcv.core;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class GraphElement {
    private UUID id;
    private Graph graph;
    private String name = "Property";
    private String description = "";
    private double x;
    private double y;
    private List<NodeCommand> commands = new ArrayList<>();

    public GraphElement(Graph graph, String name, double x, double y) {
        this.id = UUID.randomUUID();
        this.graph = graph;
        this.name = name;
        this.x = x;
        this.y = y;
    }

    protected void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public Graph getGraph() {
        return graph;
    }

    public List<NodeCommand> getCommands() {
        return commands;
    }

    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        getGraph().onChanged();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addCommand(NodeCommand command) {
        this.commands.add(command);
    }

    public abstract void onCreate() throws GraphExecutionException;
    public abstract void onDestroy() throws GraphExecutionException;
}
