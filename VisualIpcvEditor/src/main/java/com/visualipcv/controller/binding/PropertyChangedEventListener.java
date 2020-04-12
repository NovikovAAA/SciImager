package com.visualipcv.controller.binding;

@FunctionalInterface
public interface PropertyChangedEventListener {
    void onChanged(Object oldValue, Object newValue);
}
