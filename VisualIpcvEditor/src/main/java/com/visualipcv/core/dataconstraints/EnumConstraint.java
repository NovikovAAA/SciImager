package com.visualipcv.core.dataconstraints;

import com.visualipcv.core.CommonException;
import com.visualipcv.core.ValidationException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EnumConstraint extends DataTypeConstraint {
    private List<Value> values;

    public static class Value {
        private Object value;
        private Object visual;

        public Value(Object value, Object visual) {
            this.value = value;
            this.visual = visual;
        }

        public Object getValue() {
            return value;
        }

        public Object getVisual() {
            return visual;
        }

        @Override
        public String toString() {
            return visual.toString();
        }
    }

    public EnumConstraint(Value... values) {
        this.values = Arrays.asList(values);
    }

    public List<Value> getValues() {
        return values;
    }

    @Override
    public Object validate(Object value) throws ValidationException {
        for(Value v : values) {
            if(v.getValue().equals(value)) {
                return value;
            }
        }
        throw new ValidationException(EnumConstraint.class, value);
    }
}
