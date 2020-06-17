package com.visualipcv.controller;

import com.visualipcv.controller.inputfields.EnumFieldController;
import com.visualipcv.controller.inputfields.DoubleFieldController;
import com.visualipcv.controller.inputfields.IntegerFieldController;
import com.visualipcv.controller.inputfields.PathFieldController;
import com.visualipcv.controller.inputfields.StringFieldController;
import com.visualipcv.controller.inputfields.VectorNFieldController;
import com.visualipcv.core.DataType;
import com.visualipcv.core.DataTypes;
import com.visualipcv.core.dataconstraints.EnumConstraint;

public class DefaultInputFieldFactory extends InputFieldFactory {
    @Override
    public Controller<?> create(DataType type) {
        if(type.getConstraint(EnumConstraint.class) != null) {
            return new EnumFieldController(type);
        }

        if(type == DataTypes.DOUBLE) {
            return new DoubleFieldController();
        } else if(type == DataTypes.INTEGER) {
            return new IntegerFieldController();
        } else if(type == DataTypes.STRING) {
            return new StringFieldController();
        } else if(type == DataTypes.VECTOR2) {
            return new VectorNFieldController(2);
        } else if(type == DataTypes.VECTOR3) {
            return new VectorNFieldController(3);
        } else if(type == DataTypes.VECTOR4) {
            return new VectorNFieldController(4);
        } else if(type == DataTypes.FILE) {
            return new PathFieldController(false);
        } else if(type == DataTypes.DIRECTORY) {
            return new PathFieldController(true);
        }
        return null;
    }
}
