package com.visualipcv.controller;

import com.visualipcv.controller.inputfields.NumberFieldController;
import com.visualipcv.controller.inputfields.StringFieldController;
import com.visualipcv.controller.inputfields.VectorNFieldController;
import com.visualipcv.core.DataType;

public class DefaultInputFieldFactory extends InputFieldFactory {
    @Override
    public Controller<?> create(DataType type) {
        if(type == DataType.NUMBER) {
            return new NumberFieldController();
        } else if(type == DataType.STRING) {
            return new StringFieldController();
        } else if(type == DataType.VECTOR2) {
            return new VectorNFieldController(2);
        } else if(type == DataType.VECTOR3) {
            return new VectorNFieldController(3);
        } else if(type == DataType.VECTOR4) {
            return new VectorNFieldController(4);
        }
        return null;
    }
}
