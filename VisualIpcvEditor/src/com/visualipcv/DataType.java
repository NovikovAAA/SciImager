package com.visualipcv;

import java.awt.*;

public class DataType {

    public static final String NUMBER = "Number";
    public static final String VECTOR2 = "Vector2";
    public static final String VECTOR3 = "Vector3";
    public static final String VECTOR4 = "Vector4";
    public static final String IMAGE = "Image";
    public static final String FILE = "File";

    private String name;
    private Color color;

    public DataType(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }
}
