package com.visualipcv.controller;

import com.visualipcv.controller.binding.PropertyChangedEventListener;
import com.visualipcv.controller.binding.UIProperty;
import com.visualipcv.core.DataType;
import com.visualipcv.core.NodeSlot;
import com.visualipcv.core.OutputNodeSlot;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class AdvancedNodeSlotController extends Controller<HBox> {
    private NodeController controller;
    private NodeSlotController slotController = null;
    private InputFieldController fieldController = null;
    private VBox titleAndFieldBox;

    private UIProperty showControlProperty = new UIProperty(true);
    private UIProperty showConnectorProperty = new UIProperty(true);
    private UIProperty titleProperty = new UIProperty();

    public AdvancedNodeSlotController(NodeController controller, DataType type) {
        super(HBox.class);
        this.controller = controller;

        Text title = new Text();
        title.setFont(new Font(14.0));

        slotController = new NodeSlotController(controller);
        fieldController = new InputFieldController(type);

        titleAndFieldBox = new VBox();
        titleAndFieldBox.getChildren().add(title);
        titleAndFieldBox.getChildren().add(fieldController.getView());
        HBox.setMargin(titleAndFieldBox, new Insets(5.0));
        HBox.setHgrow(titleAndFieldBox, Priority.ALWAYS);
        getView().getChildren().add(slotController.getView());
        getView().getChildren().add(titleAndFieldBox);

        showControlProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                boolean output = getContext() instanceof OutputNodeSlot;
                fieldController.getView().setVisible((Boolean)newValue && !output);
                fieldController.getView().setManaged((Boolean)newValue && !output);
            }
        });

        showConnectorProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                slotController.getView().setVisible((Boolean)newValue);
                slotController.getView().setVisible((Boolean)newValue);
            }
        });

        slotController.isOutputProperty().addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                getView().getChildren().clear();

                if((Boolean)newValue) {
                    getView().getChildren().add(titleAndFieldBox);
                    getView().getChildren().add(slotController.getView());
                    titleAndFieldBox.setAlignment(Pos.CENTER_RIGHT);
                } else {
                    getView().getChildren().add(slotController.getView());
                    getView().getChildren().add(titleAndFieldBox);
                    titleAndFieldBox.setAlignment(Pos.CENTER_LEFT);
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
            return ((NodeSlot)slot).getProperty().showConnector();
        });

        showControlProperty.setBinder((Object slot) -> {
            return ((NodeSlot)slot).getProperty().showControl();
        });

        titleProperty.setBinder((Object slot) -> {
            return ((NodeSlot)slot).getProperty().getName();
        });

        if(fieldController != null && (Boolean)showControlProperty.getValue()) {
            slotController.connectedProperty().addEventListener(new PropertyChangedEventListener() {
                @Override
                public void onChanged(Object oldValue, Object newValue) {
                    if(getContext() instanceof OutputNodeSlot)
                        return;

                    if((boolean)newValue) {
                        fieldController.getView().setVisible(false);
                    } else if((boolean)showControlProperty.getValue()) {
                        fieldController.getView().setVisible(true);
                    }
                }
            });
        }

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
        fieldController.setContext(context);
        slotController.setContext(context);
        super.setContext(context);
    }

    @Override
    public void invalidate() {
        super.invalidate();

        if(slotController != null)
            slotController.invalidate();

        if(fieldController != null)
            fieldController.invalidate();
    }
}
