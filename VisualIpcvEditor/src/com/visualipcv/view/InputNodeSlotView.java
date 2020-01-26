package com.visualipcv.view;

import com.visualipcv.DataType;
import com.visualipcv.ProcessorProperty;
import com.visualipcv.view.events.InputNodeSlotEventListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class InputNodeSlotView extends JPanel {
    private NodeSlotView slotView;
    private JTextField input;

    private InputNodeSlotEventListener inputNodeSlotEventListener;

    public InputNodeSlotView(ProcessorProperty prop, NodeView nodeView) {
        super(null);
        setOpaque(false);

        slotView = new NodeSlotView(prop, nodeView, NodeSlotType.INPUT);
        add(slotView);

        JLabel title = new JLabel(prop.getName());
        title.setLocation(10 + NodeSlotView.SLOT_SIZE, 0);
        title.setSize(150, 15);
        title.setForeground(Color.WHITE);
        add(title);

        if(prop.getType() == DataType.NUMBER) {
            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
            DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
            decimalFormat.setGroupingUsed(false);

            input = new JFormattedTextField(decimalFormat);
            input.setSize(150, 20);
            input.setLocation(10 + NodeSlotView.SLOT_SIZE, 15);
            add(input);

            input.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    inputNodeSlotEventListener.onValueChanged(Integer.parseInt(input.getText()));
                }
            });

            input.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {

                }

                @Override
                public void focusLost(FocusEvent e) {
                    inputNodeSlotEventListener.onValueChanged(Integer.parseInt(input.getText()));
                }
            });
        }

        setSize(
                slotView.getSize().width + title.getSize().width + title.getLocation().x,
                Math.max(50, slotView.getHeight()));
    }

    public NodeSlotView getSlotView() {
        return slotView;
    }

    public void updateValue(Object value) {
        if(slotView.getProperty().getType() == DataType.NUMBER) {
            assert(value instanceof Integer);
            input.setText(value.toString());
        }
    }

    public void setInputNodeSlotEventListener(InputNodeSlotEventListener inputNodeSlotEventListener) {
        this.inputNodeSlotEventListener = inputNodeSlotEventListener;
    }
}
