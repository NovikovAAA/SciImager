package com.visualipcv.core;

@FunctionalInterface
public interface DataTypeConverter {
    Object convert(Object source);
}
