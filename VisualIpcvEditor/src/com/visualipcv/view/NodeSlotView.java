package com.visualipcv.view;

import com.visualipcv.NodeSlot;
import com.visualipcv.ProcessorProperty;
import com.visualipcv.view.dragdrop.SlotDragHandler;
import com.visualipcv.view.events.DragDropEventListener;

import javax.swing.*;
import javax.swing.border.StrokeBorder;
import java.awt.*;
import java.awt.dnd.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class NodeSlotView extends JComponent {
    public static final int SLOT_SIZE = 40;
    public static final int STROKE_THICKNESS = 3;
    public static final int PADDING = 8;

    private ArrayList<SlotConnection> connections = new ArrayList<>();
    private NodeView node;
    private NodeSlotType slotType;
    private ProcessorProperty property;

    private DragDropEventListener dragDropEventListener;

    public NodeSlotView(ProcessorProperty prop, NodeView node, NodeSlotType type) {
        this.slotType = type;
        this.node = node;
        this.property = prop;

        setSize(SLOT_SIZE, SLOT_SIZE);
        setBackground(new Color(
                prop.getType().getColor().getRed() / 4,
                prop.getType().getColor().getGreen() / 4,
                prop.getType().getColor().getBlue() / 4,
                255));
        setForeground(prop.getType().getColor());
        setTransferHandler(new SlotDragHandler());
        addMouseListener(new DragMouseAdapter());
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D)graphics;
        graphics2D.setColor(getBackground());
        graphics2D.fillOval(PADDING, PADDING, getWidth() - PADDING * 2, getHeight() - PADDING * 2);
        graphics2D.setColor(getForeground());
        graphics2D.setStroke(new BasicStroke(STROKE_THICKNESS));
        graphics2D.drawOval(PADDING, PADDING, getWidth() - PADDING * 2, getHeight() - PADDING * 2);

        if(!connections.isEmpty()) {
            graphics2D.setColor(getForeground());
            graphics2D.fillOval(
                    STROKE_THICKNESS + 2 + PADDING,
                    STROKE_THICKNESS + 2 + PADDING,
                    getWidth() - STROKE_THICKNESS * 2 - 4 - PADDING * 2,
                    getHeight() - STROKE_THICKNESS * 2 - 4 - PADDING * 2);
        }
    }

    public NodeSlotType getType() {
        return slotType;
    }

    public NodeView getNode() {
        return node;
    }

    public int getConnectionCount() {
        return connections.size();
    }

    public ProcessorProperty getProperty() {
        return property;
    }

    public void connect(SlotConnection connection) {
        if(getType() == NodeSlotType.INPUT && connections.size() > 1) {
            throw new IllegalArgumentException("Input slot can have only one connection");
        }
        connections.add(connection);
    }

    public SlotConnection getConnection(int index) {
        return connections.get(index);
    }

    public void disconnect(SlotConnection connection) {
        connections.remove(connection);
    }

    public int getGraphRelativeX() {
        Component current = this;
        int x = 0;

        while(current != getNode().getGraph().getInternalPanel()) {
            x += current.getX();
            current = current.getParent();
        }

        return x;
    }

    public int getGraphRelativeY() {
        Component current = this;
        int y = 0;

        while(current != getNode().getGraph().getInternalPanel()) {
            y += current.getY();
            current = current.getParent();
        }

        return y;
    }

    public void onDrop(Object payload, Point location) {
        if(dragDropEventListener != null) {
            dragDropEventListener.onDrop(payload, location);
        }
    }

    public void onBeginDrag(Object payload) {
        if(dragDropEventListener != null) {
            dragDropEventListener.onBeginDrag(payload);
        }
    }

    public void setDragDropEventListener(DragDropEventListener listener) {
        this.dragDropEventListener = listener;
    }

    public class DragMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            JComponent source = (JComponent)e.getSource();
            source.getTransferHandler().exportAsDrag(source, e, TransferHandler.LINK);
        }
    }
}
