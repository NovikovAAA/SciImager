package com.visualipcv.view;

import com.visualipcv.controller.Controller;
import com.visualipcv.controller.GraphController;
import com.visualipcv.controller.INameable;
import com.visualipcv.controller.binding.PropertyChangedEventListener;
import com.visualipcv.core.Graph;
import com.visualipcv.editor.EditorWindow;
import javafx.scene.control.Tab;

public class EditorWindowTab extends Tab {
    private Controller<?> controller;

    public EditorWindowTab(Controller<?> controller) {
        super(null, controller.getView());
        this.controller = controller;

        if(controller instanceof INameable) {
            INameable nameable = (INameable)controller;
            nameable.nameProperty().addEventListener(new PropertyChangedEventListener() {
                @Override
                public void onChanged(Object oldValue, Object newValue) {
                    String name = (String)newValue;
                    name = name.substring(Math.max(name.lastIndexOf('\\'), name.lastIndexOf('/')) + 1);
                    setText(name);
                }
            });
            controller.poll(nameable.nameProperty());
        } else {
            EditorWindow editorWindow = controller.getClass().getAnnotation(EditorWindow.class);

            if(editorWindow == null) {
                setText("<error>");
                return;
            }

            setText(editorWindow.name());
        }
    }

    public Controller<?> getController() {
        return controller;
    }
}
