package com.visualipcv.controller;

import com.visualipcv.controller.binding.PropertyChangedEventListener;
import com.visualipcv.controller.binding.UIProperty;
import com.visualipcv.core.DataType;
import com.visualipcv.core.InputNodeSlot;
import com.visualipcv.core.NodeSlot;
import com.visualipcv.core.OutputNodeSlot;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class AdvancedNodeSlotController extends Controller<HBox> {
    private NodeController controller;
    private NodeSlotController slotController = null;
    private InputFieldController fieldController = null;

    private UIProperty showControlProperty = new UIProperty();
    private UIProperty showConnectorProperty = new UIProperty();
    private UIProperty isOutputProperty = new UIProperty();
    private UIProperty titleProperty = new UIProperty();

    public AdvancedNodeSlotController(NodeController controller, DataType type) {
        super(HBox.class);
        this.controller = controller;

        Text title = new Text();
        title.setFont(new Font(14.0));

        slotController = new NodeSlotController(controller);
        fieldController = new InputFieldController(type);

        VBox vbox = new VBox();
        vbox.getChildren().add(title);
        vbox.getChildren().add(fieldController.getView());
        HBox.setMargin(vbox, new Insets(5.0));
        getView().getChildren().add(slotController.getView());
        getView().getChildren().add(vbox);

        showControlProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                fieldController.getView().setVisible((Boolean)newValue);
                fieldController.getView().setManaged((Boolean)newValue);
            }
        });

        showConnectorProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                slotController.getView().setVisible((Boolean)newValue);
                slotController.getView().setVisible((Boolean)newValue);
            }
        });

        isOutputProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                if((Boolean)newValue) {
                    getView().setAlignment(Pos.CENTER_RIGHT);
                } else {
                    getView().setAlignment(Pos.CENTER_LEFT);
                }
            }
        });

        titleProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                title.setText((String)newValue);
            }
        });

        showConnectorProperty.setBinder((Object slot) -> {
            return ((InputNodeSlot)slot).getProperty().showConnector();
        });

        showControlProperty.setBinder((Object slot) -> {
            return ((InputNodeSlot)slot).getProperty().showControl();
        });

        isOutputProperty.setBinder((Object slot) -> {
            return slot instanceof OutputNodeSlot;
        });

        titleProperty.setBinder((Object slot) -> {
            return ((NodeSlot)slot).getProperty().getName();
        });

        initialize();
    }

    public NodeSlotController getSlot() {
        return slotController;
    }

    public InputFieldController getInputFiled() {
        return fieldController;
    }

    @Override
    public void setContext(Object context) {
        super.setContext(context);
        fieldController.setContext(context);
        slotController.setContext(context);
    }
}
