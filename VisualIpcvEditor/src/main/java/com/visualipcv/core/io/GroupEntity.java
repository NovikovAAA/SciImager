package com.visualipcv.core.io;

import com.visualipcv.core.Group;

import java.io.Serializable;
import java.util.UUID;

public class GroupEntity implements Serializable {
    private UUID id;
    private double x;
    private double y;
    private double width;
    private double height;
    private String name;

    public GroupEntity(Group group) {
        this.id = group.getId();
        this.x = group.getX();
        this.y = group.getY();
        this.width = group.getWidth();
        this.height = group.getHeight();
        this.name = group.getName();
    }

    public UUID getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public String getName() {
        return name;
    }

    public void resetId() {
        id = UUID.randomUUID();
    }
}
