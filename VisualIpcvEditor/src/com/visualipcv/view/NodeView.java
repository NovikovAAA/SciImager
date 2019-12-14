package com.visualipcv.view;

import com.sun.xml.internal.ws.api.message.Header;
import com.visualipcv.DataType;
import com.visualipcv.Processor;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.MouseInputAdapter;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ContainerAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Map;

public class NodeView extends JPanel {
    private Processor processor;

    private int slotSize = 25;
    private int gaps = 10;

    private static Component createHeader(String text) {
        HeaderView title = new HeaderView();
        title.setText(text);
        title.setBackground(Color.LIGHT_GRAY);
        title.setOpaque(true);
        title.setSize(new Dimension(0, 25));
        return title;
    }

    private static Container createContentPanel(int inSlotCount, int outSlotCount, int slotSize, int gaps) {
        JPanel properties = new JPanel();
        properties.setOpaque(false);
        int count = Math.max(1, Math.max(inSlotCount, outSlotCount));
        properties.setSize(new Dimension(300, count * (slotSize + gaps) + gaps));
        return properties;
    }

    private static Container createOutputProperties(Map<String, DataType> props, int slotSize, int gaps) {
        JPanel panel = new JPanel(null);
        int index = 0;

        for(String name : props.keySet()) {
            NodeSlotView slot = new NodeSlotView();
            slot.setSize(slotSize, slotSize);
            slot.setLocation(gaps, index * slotSize + (index + 1) * gaps);
            panel.add(slot);
            index++;
        }

        panel.setSize(new Dimension(slotSize + gaps * 2, index * (slotSize + gaps)));
        panel.setPreferredSize(panel.getSize());
        panel.setOpaque(false);
        return panel;
    }

    public NodeView(Processor processor) {
        this.processor = processor;

        GridBagLayout rootLayout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        this.setLayout(rootLayout);
        this.setBackground(new Color(80, 80, 80, 200));
        rootLayout.getLayoutDimensions();

        Component header = createHeader(processor.getName());
        Container properties = createContentPanel(
                processor.getInputPropertyCount(),
                processor.getOutputPropertyCount(),
                slotSize, gaps);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.5;
        constraints.weighty = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        this.add(header, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 0.5;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;
        this.add(properties, constraints);

        this.setSize(new Dimension(properties.getWidth(), properties.getHeight() + header.getHeight()));

        Container outputs = createOutputProperties(processor.getOutputProperties(), slotSize, gaps);

        GridLayout inGrid = new GridLayout(processor.getInputPropertyCount(), 1, 5, 5);
        JPanel inputs = new JPanel(inGrid);
        inputs.setOpaque(false);
        //inputs.setSize(100, properties.getHeight());

        SpringLayout springLayout = new SpringLayout();
        properties.setLayout(springLayout);
        properties.add(inputs, constraints);
        properties.add(outputs, constraints);

        springLayout.putConstraint(SpringLayout.NORTH, inputs, 0,
                                    SpringLayout.NORTH, properties);
        springLayout.putConstraint(SpringLayout.SOUTH, inputs, 0,
                                    SpringLayout.SOUTH, properties);
        springLayout.putConstraint(SpringLayout.WEST, inputs, 0,
                                    SpringLayout.WEST, properties);
        springLayout.putConstraint(SpringLayout.NORTH, outputs, 0,
                                    SpringLayout.NORTH, properties);
        springLayout.putConstraint(SpringLayout.SOUTH, outputs, 0,
                                    SpringLayout.SOUTH, properties);
        springLayout.putConstraint(SpringLayout.EAST, outputs, 0,
                                    SpringLayout.EAST, properties);
        springLayout.putConstraint(SpringLayout.EAST, inputs, 0,
                                    SpringLayout.WEST, outputs);

        DragListener drag = new DragListener();
        this.addMouseListener(drag);
        this.addMouseMotionListener(drag);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D)graphics;
        graphics2D.setColor(getBackground());
        graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
    }
}
