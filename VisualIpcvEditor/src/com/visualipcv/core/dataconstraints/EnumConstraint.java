package com.visualipcv.core.dataconstraints;

import com.visualipcv.core.CommonException;
import com.visualipcv.core.ValidationException;

import java.util.Arrays;
import java.util.Collections;

public class EnumConstraint extends DataTypeConstraint {
    private Object[] values;

    public EnumConstraint(Object... values) {
        this.values = values;
    }

    public Object[] getValues() {
        return values;
    }

    @Override
    public Object validate(Object value) throws ValidationException {
        if(!Arrays.asList(values).contains(value)) {
            throw new ValidationException(getClass(), value);
        }
        return value;
    }
}
