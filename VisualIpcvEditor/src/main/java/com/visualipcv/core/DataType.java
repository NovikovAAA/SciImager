package com.visualipcv.core;

import com.visualipcv.core.annotations.PrimaryDataType;
import com.visualipcv.core.annotations.RegisterDataType;
import com.visualipcv.core.dataconstraints.DataTypeConstraint;
import javafx.scene.paint.Color;
import org.opencv.core.Mat;

import javax.xml.datatype.DatatypeConstants;
import java.util.Objects;

public abstract class DataType {
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
