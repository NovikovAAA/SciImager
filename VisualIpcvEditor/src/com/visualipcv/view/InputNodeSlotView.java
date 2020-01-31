package com.visualipcv.view;

import com.visualipcv.core.DataType;
import com.visualipcv.core.ProcessorProperty;
import com.visualipcv.view.events.InputNodeSlotEventListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.util.Locale;

public class InputNodeSlotView extends JPanel {
    private NodeSlotView slotView;
    private JTextField input1;
    private JTextField input2;
    private JTextField input3;
    private JTextField input4;

    private ProcessorProperty property;
    private InputNodeSlotEventListener inputNodeSlotEventListener;

    public InputNodeSlotView(ProcessorProperty prop, NodeView nodeView) {
        super(null);

        this.property = prop;
        setOpaque(false);

        if(prop.showConnector()) {
            slotView = new NodeSlotView(prop, nodeView, NodeSlotType.INPUT);
            add(slotView);
        }

        int leftOffset = prop.showConnector() ? NodeSlotView.SLOT_SIZE : 0;
        leftOffset += 10;

        JLabel title = new JLabel(prop.getName());
        title.setLocation(leftOffset, 0);
        title.setSize(150, 15);
        title.setForeground(Color.WHITE);
        add(title);

        if(prop.showControl()) {
            createControl();
        }

        setSize(NodeSlotView.SLOT_SIZE + title.getSize().width + title.getLocation().x,
                Math.max(50, NodeSlotView.SLOT_SIZE));
    }

    private void createControl() {
        int leftOffset = property.showConnector() ? NodeSlotView.SLOT_SIZE : 0;
        leftOffset += 10;

        if(property.getType().equals(DataType.NUMBER)) {
            input1 = createNumberField();
            input1.setSize(150, 20);
            input1.setLocation(leftOffset, 15);
        } else if(property.getType().equals(DataType.STRING)) {
            input1 = createTextFiled(null);
            input1.setSize(150, 20);
            input1.setLocation(leftOffset, 15);
        } else if(property.getType().equals(DataType.VECTOR2)) {
            input1 = createNumberField();
            input1.setSize(70, 20);
            input1.setLocation(leftOffset, 15);

            input2 = createNumberField();
            input2.setSize(70, 20);
            input2.setLocation(leftOffset + 80, 15);
        } else if(property.getType().equals(DataType.VECTOR3)) {
            input1 = createNumberField();
            input1.setSize(43, 20);
            input1.setLocation(leftOffset, 15);

            input2 = createNumberField();
            input2.setSize(43, 20);
            input2.setLocation(leftOffset + 53, 15);

            input3 = createNumberField();
            input3.setSize(43, 20);
            input3.setLocation(leftOffset + 106, 15);
        } else if(property.getType().equals(DataType.VECTOR4)) {
            input1 = createNumberField();
            input1.setSize(30, 20);
            input1.setLocation(leftOffset, 15);

            input2 = createNumberField();
            input2.setSize(30, 20);
            input2.setLocation(leftOffset + 40, 15);

            input3 = createNumberField();
            input3.setSize(30, 20);
            input3.setLocation(leftOffset + 80, 15);

            input4 = createNumberField();
            input4.setSize(30, 20);
            input4.setLocation(leftOffset + 120, 15);
        }
    }

    public NodeSlotView getSlotView() {
        return slotView;
    }

    public ProcessorProperty getProperty() {
        return property;
    }

    public void updateValue(Object value) {
        if(!property.showControl()) {
            return;
        }

        if(property.getType().equals(DataType.NUMBER)) {
            assert (value instanceof Double);
            input1.setText(value.toString());
        } else if(property.getType().equals(DataType.STRING)) {
            assert(value instanceof String);
            input1.setText(value.toString());
        } else if(property.getType().equals(DataType.VECTOR2)) {
            assert(value instanceof Double[]);
            input1.setText(((Double[])value)[0].toString());
            input2.setText(((Double[])value)[1].toString());
        } else if(property.getType().equals(DataType.VECTOR3)) {
            assert(value instanceof Double[]);
            input1.setText(((Double[])value)[0].toString());
            input2.setText(((Double[])value)[1].toString());
            input3.setText(((Double[])value)[2].toString());
        } else if(property.getType().equals(DataType.VECTOR4)) {
            assert(value instanceof  Double[]);
            input1.setText(((Double[])value)[0].toString());
            input2.setText(((Double[])value)[1].toString());
            input3.setText(((Double[])value)[2].toString());
            input4.setText(((Double[])value)[3].toString());
        }
    }

    public void setInputNodeSlotEventListener(InputNodeSlotEventListener inputNodeSlotEventListener) {
        this.inputNodeSlotEventListener = inputNodeSlotEventListener;
    }

    public void onValueChanged() {
        if(!property.showControl()) {
            return;
        }

        if(property.getType().equals(DataType.NUMBER)) {
            inputNodeSlotEventListener.onValueChanged(Double.parseDouble(input1.getText()));
        } else if(property.getType().equals(DataType.STRING)) {
            inputNodeSlotEventListener.onValueChanged(input1.getText());
        } else if(property.getType().equals(DataType.VECTOR2)) {
            Double[] values = new Double[2];
            values[0] = Double.parseDouble(input1.getText());
            values[1] = Double.parseDouble(input2.getText());
            inputNodeSlotEventListener.onValueChanged(values);
        } else if(property.getType().equals(DataType.VECTOR3)) {
            Double[] values = new Double[3];
            values[0] = Double.parseDouble(input1.getText());
            values[1] = Double.parseDouble(input2.getText());
            values[2] = Double.parseDouble(input3.getText());
            inputNodeSlotEventListener.onValueChanged(values);
        } else if(property.getType().equals(DataType.VECTOR4)) {
            Double[] values = new Double[4];
            values[0] = Double.parseDouble(input1.getText());
            values[1] = Double.parseDouble(input2.getText());
            values[2] = Double.parseDouble(input3.getText());
            values[3] = Double.parseDouble(input4.getText());
            inputNodeSlotEventListener.onValueChanged(values);
        }
    }

    private JTextField createTextFiled(Format format) {
        JTextField input = new JFormattedTextField(format);
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

    private JTextField createNumberField() {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        decimalFormat.setGroupingUsed(false);
        return createTextFiled(decimalFormat);
    }
}
