package com.visualipcv.controller;

import com.visualipcv.core.DataType;
import javafx.scene.layout.Pane;

public class InputFieldController extends Controller<Pane> {
    public static final double STD_WIDTH = 180.0;
    public static final double STD_HEIGHT = 25.0;
    public static final double STD_MARGIN = 10.0;
    public static final double STD_TEXT_PADDING = 3.0;
    public static final double STD_FONT_SIZE = 14.0;

    private static InputFieldFactory factory = new DefaultInputFieldFactory();
    private Controller<?> controller;

    InputFieldController(DataType type) {
        super(Pane.class);
        controller = factory.create(type);

        if(controller != null)
            getView().getChildren().add(controller.getView());
    }

    @Override
    public void setContext(Object context) {
        super.setContext(context);

        if(controller != null)
            controller.setContext(context);
    }

    public static InputFieldFactory getFactory() {
        return factory;
    }

    public static void setFactory(InputFieldFactory factory) {
        InputFieldController.factory = factory;
    }
}
