package com.visualipcv.controller;

import com.visualipcv.core.DataType;
import com.visualipcv.core.GraphExecutionData;
import com.visualipcv.core.InputNodeSlot;
import com.visualipcv.core.NodeSlot;
import javafx.scene.layout.Pane;

public class InputFieldController extends Controller<Pane> {
    public static final double STD_WIDTH = 160.0;
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

    public static Object getValueFromSlot(NodeSlot slot) {
        if(slot instanceof InputNodeSlot) {
            Object value = ((InputNodeSlot)slot).getValue();

            if(value != null)
                return value;
        }

        Object value = GraphExecutionData.load(slot);

        if(value != null)
            return value;

        value = slot.getProperty().getType().getDefaultValue();
        return value;
    }
}
