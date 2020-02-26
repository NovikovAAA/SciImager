package com.visualipcv.view;

import com.visualipcv.view.scriptconstruction.PropertyStackView;
import com.visualipcv.viewmodel.ViewModel;
import javafx.beans.property.Property;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class ViewBase<T extends Node> {
    private T view;
    private ViewModel context;

    public ViewBase(Class<T> clazz) {
        try {
            view = clazz.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    public ViewBase(Class<T> clazz, String fxmlPath) {
        this(clazz);

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlPath));
        loader.setRoot(view);
        loader.setController(this);

        try {
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public T getView() {
        return view;
    }

    public void setContext(ViewModel context) {
        this.context = context;
        Field[] fields = getClass().getDeclaredFields();

        for(Field field : fields) {
            field.setAccessible(true);
            Binding binding = field.getAnnotation(Binding.class);

            if(binding == null)
                continue;

            Property<Object> target = null;

            try {
                Field targetField = context.getClass().getDeclaredField(binding.value());
                targetField.setAccessible(true);
                Object value = targetField.get(context);

                if(!(value instanceof Property))
                    continue;

                target = (Property<Object>)targetField.get(context);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

            if(target == null)
                continue;

            try {
                Property<Object> prop = (Property<Object>)field.get(this);

                if(binding.type() == BindingType.Bidirectional)
                    prop.bindBidirectional(target);
                else if(binding.type() == BindingType.FromSource)
                    prop.bind(target);
                else if(binding.type() == BindingType.ToSource) {
                    prop.setValue(target.getValue());
                    target.bind(prop);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
