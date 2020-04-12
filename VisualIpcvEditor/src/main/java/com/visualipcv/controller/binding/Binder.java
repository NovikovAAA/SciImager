package com.visualipcv.controller.binding;

@FunctionalInterface
public interface Binder<T> {
    public Object update(T context);
}
