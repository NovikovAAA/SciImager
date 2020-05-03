package com.visualipcv.controller;

import com.visualipcv.controller.binding.PropertyChangedEventListener;
import com.visualipcv.controller.binding.UIProperty;
import com.visualipcv.editor.EditorWindow;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Region;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Controller<T extends Node> {
    private T view;
    private Object context;

    private List<UIProperty> properties = new ArrayList<>();
    private List<PropertyChangedEventListener> callbacks = new ArrayList<>();

    private void setDefaultSize() {
        EditorWindow windowData = getClass().getAnnotation(EditorWindow.class);

        if(windowData != null) {
            ((Region)getView()).setPrefWidth(windowData.prefWidth());
            ((Region)getView()).setPrefHeight(windowData.prefHeight());
        }
    }

    public Controller(Class<T> clazz) {
        try {
            view = clazz.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        setDefaultSize();
    }

    public Controller(Class<T> clazz, String fxmlPath) {
        this(clazz);

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlPath));
        loader.setRoot(view);
        loader.setController(this);

        try {
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        setDefaultSize();
    }

    private void addProperties(Class<?> clazz) {
        if(clazz.getSuperclass() == null) {
            return;
        }

        for(Field field : clazz.getDeclaredFields()) {
            if(field.getType() == UIProperty.class) {
                field.setAccessible(true);

                try {
                    properties.add((UIProperty)field.get(this));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        addProperties(clazz.getSuperclass());
    }

    protected void initialize() {
        addProperties(getClass());
    }

    public T getView() {
        return view;
    }

    public void setContext(Object context) {
        this.context = context;
        invalidate();
    }

    public <M> M getContext() {
        return (M)context;
    }

    public void poll(UIProperty property) {
        if(property.getBinder() == null)
            return;

        property.setValue(property.getBinder().update(getContext()));
    }

    public void invalidate() {
        for(UIProperty property : properties) {
            poll(property);
        }
    }

    public void addPropertyChangedEventListener(PropertyChangedEventListener propertyChangedEventListener) {
        callbacks.add(propertyChangedEventListener);
    }
}
