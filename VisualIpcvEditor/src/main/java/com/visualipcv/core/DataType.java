package com.visualipcv.core;

import com.visualipcv.core.annotations.PrimaryDataType;
import com.visualipcv.core.annotations.RegisterDataType;
import com.visualipcv.core.dataconstraints.DataTypeConstraint;
import org.opencv.core.Mat;

import javax.xml.datatype.DatatypeConstants;
import java.awt.*;
import java.util.Objects;

public abstract class DataType {

    @PrimaryDataType
    @RegisterDataType
    public static final DataType ANY = new DataType("Any", Color.GRAY, Object.class) {
        @Override
        public Object getDefaultValue() {
            return null;
        }
    };

    @PrimaryDataType
    @RegisterDataType
    public static final DataType DOUBLE = new DataType("Double", Color.GREEN, Double.class) {
        @Override
        public Object getDefaultValue() {
            return 0.0;
        }
    };

    @PrimaryDataType
    @RegisterDataType
    public static final DataType INTEGER = new DataType("Integer", Color.GREEN, Integer.class) {
        @Override
        public Object getDefaultValue() {
            return 0;
        }
    };

    @RegisterDataType
    public static final DataType VECTOR2 = new DataType("Vector2", Color.ORANGE, Double[].class) {
        @Override
        public Object getDefaultValue() {
            return new Double[] { 0.0, 0.0 };
        }
    };

    @RegisterDataType
    public static final DataType VECTOR3 = new DataType("Vector3", Color.ORANGE, Double[].class) {
        @Override
        public Object getDefaultValue() {
            return new Double[] { 0.0, 0.0, 0.0 };
        }
    };

    @RegisterDataType
    public static final DataType VECTOR4 = new DataType("Vector4", Color.ORANGE, Double[].class) {
        @Override
        public Object getDefaultValue() {
            return new Double[] { 0.0, 0.0, 0.0, 0.0 };
        }
    };

    @PrimaryDataType
    @RegisterDataType
    public static final DataType IMAGE = new DataType("Image", Color.WHITE, Mat.class) {
        @Override
        public Object getDefaultValue() {
            return null;
        }
    };

    @PrimaryDataType
    @RegisterDataType
    public static final DataType BYTES = new DataType("Bytes", Color.WHITE, byte[].class) {
        @Override
        public Object getDefaultValue() {
            return null;
        }
    };

    @PrimaryDataType
    @RegisterDataType
    public static final DataType STRING = new DataType("String", Color.MAGENTA, String.class) {
        @Override
        public Object getDefaultValue() {
            return "";
        }
    };

    private String name;
    private Color color;
    private boolean isPrimary = false;
    private Class<?> clazz;
    private DataTypeConstraint[] constraints = new DataTypeConstraint[0];

    public DataType(String name, Color color, Class<?> clazz, DataTypeConstraint... constraints) {
        this.name = name;
        this.color = color;
        this.clazz = clazz;
        this.constraints = constraints;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary() {
        isPrimary = true;
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

    public Object validate(Object value) throws ValidationException {
        for(DataTypeConstraint constraint : constraints) {
            value = constraint.validate(value);
        }
        return value;
    }

    public DataTypeConstraint[] getConstraints() {
        return constraints;
    }

    public <T extends DataTypeConstraint> T getConstraint(Class<T> clazz) {
        for(DataTypeConstraint constraint : constraints) {
            if(constraint.getClass() == clazz)
                return (T)constraint;
        }
        return null;
    }
}
