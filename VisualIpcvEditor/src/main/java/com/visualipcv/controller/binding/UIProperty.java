package com.visualipcv.controller.binding;

import java.util.ArrayList;
import java.util.List;

public class UIProperty {
    private Object value;
    private Binder binder;

    private List<PropertyChangedEventListener> callbacks = new ArrayList<>();

    public UIProperty() {

    }

    public UIProperty(Object value) {
        this.value = value;
    }

    public UIProperty(Object value, Binder binder) {
        this.value = value;
        this.binder = binder;
    }

    public void setValue(Object value) {
        Object prevValue = this.value;
        this.value = value;
        onChanged(prevValue, value);
    }

    private void onChanged(Object oldValue, Object newValue) {
        for(PropertyChangedEventListener eventListener : callbacks) {
            eventListener.onChanged(oldValue, newValue);
        }
    }

    public Object getValue() {
        return value;
    }

    public void setBinder(Binder binder) {
        this.binder = binder;
    }

    public Binder getBinder() {
        return binder;
    }

    public void addEventListener(PropertyChangedEventListener propertyChangedEventListener) {
        callbacks.add(propertyChangedEventListener);
    }

    public void removeEventListener(PropertyChangedEventListener propertyChangedEventListener) {
        callbacks.remove(propertyChangedEventListener);
    }
}
