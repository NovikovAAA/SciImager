package com.visualipcv.controller;

import com.visualipcv.controller.binding.Binder;
import com.visualipcv.controller.binding.PropertyChangedEventListener;
import com.visualipcv.controller.binding.UIProperty;
import com.visualipcv.core.Group;
import javafx.scene.layout.AnchorPane;

public class GroupController extends GraphElementController<AnchorPane> {
    private final UIProperty widthProperty = new UIProperty();
    private final UIProperty heightProperty = new UIProperty();

    public GroupController(GraphController graphController) {
        super(graphController, AnchorPane.class, "GroupView.fxml");

        widthProperty.setBinder(new Binder() {
            @Override
            public Object update(Object context) {
                return ((Group)context).getWidth();
            }
        });

        heightProperty.setBinder(new Binder() {
            @Override
            public Object update(Object context) {
                return ((Group)context).getHeight();
            }
        });

        widthProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                getView().setPrefWidth((Double)newValue);
            }
        });

        heightProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                getView().setPrefHeight((Double)newValue);
            }
        });

        initialize();
    }
}
