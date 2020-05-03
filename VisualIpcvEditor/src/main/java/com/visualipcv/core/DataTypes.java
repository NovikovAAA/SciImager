package com.visualipcv.core;

import com.visualipcv.core.annotations.PrimaryDataType;
import com.visualipcv.core.annotations.RegisterDataType;
import javafx.scene.paint.Color;
import org.opencv.core.Mat;

import java.nio.file.Path;
import java.nio.file.Paths;

public class DataTypes {
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
    public static final DataType DOUBLE = new DataType("Double", Color.LIME, Double.class) {
        @Override
        public Object getDefaultValue() {
            return 0.0;
        }
    };

    @PrimaryDataType
    @RegisterDataType
    public static final DataType INTEGER = new DataType("Integer", Color.LIME, Integer.class) {
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

    @PrimaryDataType
    @RegisterDataType
    public static final DataType PATH = new DataType("Path", Color.DARKMAGENTA, Path.class) {
        @Override
        public Object getDefaultValue() {
            return Paths.get("").toAbsolutePath();
        }
    };
}
