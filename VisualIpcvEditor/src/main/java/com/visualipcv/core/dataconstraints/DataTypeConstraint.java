package com.visualipcv.core.dataconstraints;

import com.visualipcv.core.ValidationException;

public abstract class DataTypeConstraint {
    public abstract Object validate(Object value) throws ValidationException;
}
