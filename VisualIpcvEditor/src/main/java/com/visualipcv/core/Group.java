package com.visualipcv.core;

import com.visualipcv.core.io.GroupEntity;

public class Group extends GraphElement {
    private Double width = null;
    private Double height = null;

    public Group(Graph graph, double x, double y, double width, double height) {
        super(graph, "Group", x, y);
        this.width = width;
        this.height = height;
    }

    public Group(Graph graph, GroupEntity entity) {
        super(graph, entity.getName(), entity.getX(), entity.getY());
        this.width = entity.getWidth();
        this.height = entity.getHeight();
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public Double getWidth() {
        return width;
    }

    public Double getHeight() {
        return height;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {

    }
}
