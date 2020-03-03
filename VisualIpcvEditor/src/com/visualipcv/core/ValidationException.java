package com.visualipcv.core;

import com.visualipcv.core.dataconstraints.DataTypeConstraint;

public class ValidationException extends CommonException {
    public ValidationException(Class<? extends DataTypeConstraint> clazz, Object value) {
        super(clazz.getName() + ": validation failed " + value);
    }
}
