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
    private JTextField input1;
    private JTextField input2;
    private JTextField input3;
    private JTextField input4;

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
            input1 = createNumberField();
            input1.setSize(150, 20);
            input1.setLocation(10 + NodeSlotView.SLOT_SIZE, 15);
        } else if(prop.getType() == DataType.VECTOR2) {
            input1 = createNumberField();
            input1.setSize(70, 20);
            input1.setLocation(10 + NodeSlotView.SLOT_SIZE, 15);

            input2 = createNumberField();
            input2.setSize(70, 20);
            input2.setLocation(10 + NodeSlotView.SLOT_SIZE + 80, 15);
        } else if(prop.getType() == DataType.VECTOR3) {
            input1 = createNumberField();
            input1.setSize(43, 20);
            input1.setLocation(10 + NodeSlotView.SLOT_SIZE, 15);

            input2 = createNumberField();
            input2.setSize(43, 20);
            input2.setLocation(10 + NodeSlotView.SLOT_SIZE + 53, 15);

            input3 = createNumberField();
            input3.setSize(43, 20);
            input3.setLocation(10 + NodeSlotView.SLOT_SIZE + 106, 15);
        } else if(prop.getType() == DataType.VECTOR4) {
            input1 = createNumberField();
            input1.setSize(30, 20);
            input1.setLocation(10 + NodeSlotView.SLOT_SIZE, 15);

            input2 = createNumberField();
            input2.setSize(30, 20);
            input2.setLocation(10 + NodeSlotView.SLOT_SIZE + 40, 15);

            input3 = createNumberField();
            input3.setSize(30, 20);
            input3.setLocation(10 + NodeSlotView.SLOT_SIZE + 80, 15);

            input4 = createNumberField();
            input4.setSize(30, 20);
            input4.setLocation(10 + NodeSlotView.SLOT_SIZE + 120, 15);
        } else if(prop.getType() == DataType.IMAGE) {

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
            assert(value instanceof Float);
            input1.setText(value.toString());
        } else if(slotView.getProperty().getType() == DataType.VECTOR2) {
            assert(value instanceof Float[]);
            input1.setText(((Float[])value)[0].toString());
            input2.setText(((Float[])value)[1].toString());
        } else if(slotView.getProperty().getType() == DataType.VECTOR3) {
            assert(value instanceof Float[]);
            input1.setText(((Float[])value)[0].toString());
            input2.setText(((Float[])value)[1].toString());
            input3.setText(((Float[])value)[2].toString());
        } else if(slotView.getProperty().getType() == DataType.VECTOR4) {
            assert(value instanceof  Float[]);
            input1.setText(((Float[])value)[0].toString());
            input2.setText(((Float[])value)[1].toString());
            input3.setText(((Float[])value)[2].toString());
            input4.setText(((Float[])value)[3].toString());
        }
    }

    public void setInputNodeSlotEventListener(InputNodeSlotEventListener inputNodeSlotEventListener) {
        this.inputNodeSlotEventListener = inputNodeSlotEventListener;
    }

    public void onValueChanged() {
        if(slotView.getProperty().getType() == DataType.NUMBER) {
            inputNodeSlotEventListener.onValueChanged(Float.parseFloat(input1.getText()));
        } else if(slotView.getProperty().getType() == DataType.VECTOR2) {
            Float[] values = new Float[2];
            values[0] = Float.parseFloat(input1.getText());
            values[1] = Float.parseFloat(input2.getText());
            inputNodeSlotEventListener.onValueChanged(values);
        } else if(slotView.getProperty().getType() == DataType.VECTOR3) {
            Float[] values = new Float[3];
            values[0] = Float.parseFloat(input1.getText());
            values[1] = Float.parseFloat(input2.getText());
            values[2] = Float.parseFloat(input3.getText());
            inputNodeSlotEventListener.onValueChanged(values);
        } else if(slotView.getProperty().getType() == DataType.VECTOR4) {
            Float[] values = new Float[4];
            values[0] = Float.parseFloat(input1.getText());
            values[1] = Float.parseFloat(input2.getText());
            values[2] = Float.parseFloat(input3.getText());
            values[3] = Float.parseFloat(input4.getText());
            inputNodeSlotEventListener.onValueChanged(values);
        }
    }

    public JTextField createNumberField() {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        decimalFormat.setGroupingUsed(false);

        JTextField input = new JFormattedTextField(decimalFormat);
        add(input);

        input.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onValueChanged();
            }
        });

        input.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                onValueChanged();
            }
        });

        return input;
    }
}
