package com.visualipcv;

import java.awt.*;

public enum DataType {

    IMAGE {
        @Override
        public Color getColor() {
            return Color.WHITE;
        }
    },

    NUMBER {
        @Override
        public Color getColor() {
            return Color.GREEN;
        }
    },

    VECTOR2 {
        @Override
        public Color getColor() {
            return Color.ORANGE;
        }
    },

    VECTOR3 {
        @Override
        public Color getColor() {
            return Color.ORANGE;
        }
    },

    VECTOR4 {
        @Override
        public Color getColor() {
            return Color.ORANGE;
        }
    },

    MATRIX4x4 {
        @Override
        public Color getColor() {
            return Color.BLUE;
        }
    };

    public abstract Color getColor();
}
