package com.visualipcv.view;

import com.visualipcv.NodeSlot;
import com.visualipcv.Processor;
import com.visualipcv.ProcessorProperty;
import com.visualipcv.view.events.NodeEventListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NodeView extends JPanel {
    public static final int GAP_SIZE = 10;

    private GraphView graph;

    private ArrayList<InputNodeSlotView> inputSlots = new ArrayList<>();
    private ArrayList<NodeSlotView> outputSlots = new ArrayList<>();

    private NodeEventListener nodeEventListener;

    private Component createHeader(String text) {
        HeaderView title = new HeaderView();
        title.setText(text);
        title.setBackground(Color.GRAY);
        title.setForeground(Color.WHITE);
        title.setOpaque(true);
        title.setSize(new Dimension(0, 25));
        int fontSize = Math.min(25, (int)(title.getFont().getSize() * 1.5f));
        Font font = new Font(title.getFont().getFontName(), Font.PLAIN, fontSize);
        title.setFont(font);
        return title;
    }

    private Container createContentPanel(int inSlotCount, int outSlotCount) {
        JPanel properties = new JPanel();
        properties.setOpaque(false);
        int count = Math.max(1, Math.max(inSlotCount, outSlotCount));
        properties.setSize(new Dimension(300, count * (NodeSlotView.SLOT_SIZE + GAP_SIZE) + GAP_SIZE));
        return properties;
    }

    private Container createOutputProperties(List<ProcessorProperty> props) {
        JPanel panel = new JPanel(null);

        for(int i = 0; i < props.size(); i++) {
            NodeSlotView slot = new NodeSlotView(props.get(i), this, NodeSlotType.OUTPUT);
            slot.setLocation(GAP_SIZE, i * NodeSlotView.SLOT_SIZE + (i + 1) * GAP_SIZE);
            panel.add(slot);
            outputSlots.add(slot);
        }

        panel.setSize(new Dimension(NodeSlotView.SLOT_SIZE + GAP_SIZE * 2, props.size() * (NodeSlotView.SLOT_SIZE + GAP_SIZE)));
        panel.setPreferredSize(panel.getSize());
        panel.setOpaque(false);
        return panel;
    }

    private Container createInputProperties(List<ProcessorProperty> props) {
        JPanel panel = new JPanel(null);
        int width = 0;

        for(int i = 0; i < props.size(); i++) {
            InputNodeSlotView slotView = new InputNodeSlotView(props.get(i), this);
            slotView.setLocation(GAP_SIZE, i * NodeSlotView.SLOT_SIZE + (i + 1) * GAP_SIZE);
            panel.add(slotView);
            inputSlots.add(slotView);

            width = Math.max(width, slotView.getWidth());
        }

        panel.setOpaque(false);
        panel.setSize(new Dimension(width, props.size() * (NodeSlotView.SLOT_SIZE + GAP_SIZE)));
        return panel;
    }

    public NodeView(Processor processor, GraphView graph) {
        this.graph = graph;

        GridBagLayout rootLayout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        this.setLayout(rootLayout);
        this.setBackground(new Color(80, 80, 80, 200));
        rootLayout.getLayoutDimensions();

        Component header = createHeader(processor.getName());
        Container properties = createContentPanel(
                processor.getInputProperties().size(),
                processor.getOutputProperties().size());

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

        Container outputs = createOutputProperties(processor.getOutputProperties());
        Container inputs = createInputProperties(processor.getInputProperties());

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

        DragListener drag = new DragListener() {
            @Override
            public void dragged(int deltaX, int deltaY) {
                if(nodeEventListener != null) {
                    nodeEventListener.onMove(NodeView.this, deltaX, deltaY);
                }
            }
        };

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

    public GraphView getGraph() {
        return graph;
    }

    private void repaintConnections() {
        for(InputNodeSlotView slot : inputSlots) {
            for(int i = 0; i < slot.getSlotView().getConnectionCount(); i++) {
                if(slot.getSlotView().getConnection(i) != null) {
                    slot.getSlotView().getConnection(i).updateBounds();
                    slot.getSlotView().getConnection(i).repaint();
                }
            }
        }
        for(NodeSlotView slot : outputSlots) {
            for(int i = 0; i < slot.getConnectionCount(); i++) {
                if(slot.getConnection(i) != null) {
                    slot.getConnection(i).updateBounds();
                    slot.getConnection(i).repaint();
                }
            }
        }
    }

    @Override
    public void setLocation(Point p) {
        super.setLocation(p);
        repaintConnections();
    }

    @Override
    public void setLocation(int x, int y) {
        super.setLocation(x, y);
        repaintConnections();
    }

    @Override
    public void setSize(Dimension d) {
        super.setSize(d);
        repaintConnections();
    }

    @Override
    public void setSize(int x, int y) {
        super.setSize(x, y);
        repaintConnections();
    }

    public void setNodeEventListener(NodeEventListener listener) {
        nodeEventListener = listener;
    }

    public List<InputNodeSlotView> getInputSlots() {
        return inputSlots;
    }

    public List<NodeSlotView> getOutputSlots() {
        return outputSlots;
    }
}
