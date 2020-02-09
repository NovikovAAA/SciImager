package com.visualipcv.view;

import com.visualipcv.core.NodeSlot;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class AdvancedNodeSlotView extends HBox {
    private NodeSlotView slotView = null;
    private InputFieldView fieldView = null;

    public AdvancedNodeSlotView(NodeView view, NodeSlot slot, boolean isOutput) {
        setAlignment(isOutput ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

        if(slot.getProperty().showConnector()) {
            slotView = new NodeSlotView(view, slot);
        }

        if(slot.getProperty().showControl()) {
            fieldView = new InputFieldView(slot);
            setMargin(fieldView, new Insets(10.0));
        }

        Text title = new Text();
        title.setFont(new Font(14.0));
        title.setText(slot.getProperty().getName());

        VBox vbox = new VBox();
        vbox.getChildren().add(title);
        HBox.setMargin(vbox, new Insets(5.0));

        if(fieldView != null) {
            vbox.getChildren().add(fieldView);

            if(slotView != null)
                fieldView.visibleProperty().bind(slotView.getViewModel().getIsConnectedProperty().not());
        }

        if(isOutput) {
            getChildren().add(vbox);

            if(slotView != null)
                getChildren().add(slotView);
        } else {
            if(slotView != null)
                getChildren().add(slotView);

            getChildren().add(vbox);
        }
    }

    public NodeSlotView getSlotView() {
        return slotView;
    }

    public InputFieldView getFieldView() {
        return fieldView;
    }
}
