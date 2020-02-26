package com.visualipcv.core;

import java.awt.*;
import java.util.Objects;

public abstract class DataType {

    public static final DataType NUMBER = new DataType("Number", Color.GREEN) {
        @Override
        public Object getDefaultValue() {
            return 0.0;
        }
    };

    public static final DataType VECTOR2 = new DataType("Vector2", Color.ORANGE) {
        @Override
        public Object getDefaultValue() {
            return new Double[] { 0.0, 0.0 };
        }
    };

    public static final DataType VECTOR3 = new DataType("Vector3", Color.ORANGE) {
        @Override
        public Object getDefaultValue() {
            return new Double[] { 0.0, 0.0, 0.0 };
        }
    };

    public static final DataType VECTOR4 = new DataType("Vector4", Color.ORANGE) {
        @Override
        public Object getDefaultValue() {
            return new Double[] { 0.0, 0.0, 0.0, 0.0 };
        }
    };

    public static final DataType IMAGE = new DataType("Image", Color.WHITE) {
        @Override
        public Object getDefaultValue() {
            return null;
        }
    };

    public static final DataType BYTES = new DataType("Bytes", Color.WHITE) {
        @Override
        public Object getDefaultValue() {
            return null;
        }
    };

    public static final DataType STRING = new DataType("String", Color.MAGENTA) {
        @Override
        public Object getDefaultValue() {
            return "";
        }
    };

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

    public abstract Object getDefaultValue();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataType dataType = (DataType) o;
        return Objects.equals(name, dataType.name) &&
                Objects.equals(color, dataType.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color);
    }

    @Override
    public String toString() {
        return name;
    }
}
