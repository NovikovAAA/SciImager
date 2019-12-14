package com.visualipcv.view;

import com.sun.xml.internal.ws.api.message.Header;
import com.visualipcv.Processor;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.MouseInputAdapter;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class NodeView extends JPanel {
    private Processor processor;

    private JPanel inputProperties;
    private JPanel outputProperties;

    public NodeView(Processor processor) {
        this.processor = processor;

        HeaderView title = new HeaderView();
        title.setText(processor.getName());
        title.setBackground(Color.LIGHT_GRAY);
        title.setOpaque(true);
        title.setPreferredSize(new Dimension(0, 20));

        this.add(title);
        this.setSize(300, 200);
        //this.setPreferredSize(new Dimension(300, 0));
        this.setBackground(Color.GRAY);

        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);

        JPanel properties = new JPanel();
        properties.setOpaque(false);
        this.add(properties);

        layout.putConstraint(SpringLayout.NORTH, title, 0,
                            SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.WEST, title, 0,
                            SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.EAST, title, 0,
                            SpringLayout.EAST, this);

        layout.putConstraint(SpringLayout.SOUTH, properties, 0,
                            SpringLayout.SOUTH, this);
        layout.putConstraint(SpringLayout.WEST, properties, 0,
                            SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.EAST, properties, 0,
                            SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.NORTH, properties, 0,
                            SpringLayout.SOUTH, title);

        outputProperties = new JPanel(null);
        BoxLayout outLayout = new BoxLayout(outputProperties, BoxLayout.Y_AXIS);
        outputProperties.setLayout(outLayout);
        outputProperties.setPreferredSize(new Dimension(50, 0));
        outputProperties.setOpaque(false);
        properties.add(outputProperties);

        for(int i = 0; i < /*processor.getOutputPropertyCount()*/2; i++) {
            NodeSlotView slot = new NodeSlotView();
            outputProperties.add(slot);
        }

        GridLayout inGrid = new GridLayout(processor.getInputPropertyCount(), 1, 5, 5);
        inputProperties = new JPanel(inGrid);
        inputProperties.setOpaque(false);
        properties.add(inputProperties);

        SpringLayout propertiesLayout = new SpringLayout();
        properties.setLayout(propertiesLayout);
        propertiesLayout.putConstraint(SpringLayout.NORTH, outputProperties, 5,
                                        SpringLayout.NORTH, properties);
        propertiesLayout.putConstraint(SpringLayout.SOUTH, outputProperties, -5,
                                        SpringLayout.SOUTH, properties);
        propertiesLayout.putConstraint(SpringLayout.EAST, outputProperties, -5,
                                        SpringLayout.EAST, properties);

        propertiesLayout.putConstraint(SpringLayout.NORTH, inputProperties, 5,
                                        SpringLayout.NORTH, properties);
        propertiesLayout.putConstraint(SpringLayout.SOUTH, inputProperties, -5,
                                        SpringLayout.SOUTH, properties);
        propertiesLayout.putConstraint(SpringLayout.WEST, inputProperties, 5,
                                        SpringLayout.WEST, properties);
        propertiesLayout.putConstraint(SpringLayout.EAST, inputProperties, -5,
                                        SpringLayout.WEST, outputProperties);

        DragListener drag = new DragListener();
        this.addMouseListener(drag);
        this.addMouseMotionListener(drag);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D)graphics;
        graphics2D.setColor(getBackground());
        graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
    }
}
