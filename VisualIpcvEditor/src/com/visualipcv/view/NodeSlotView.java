package com.visualipcv.view;

import com.visualipcv.view.dragdrop.SlotDragHandler;
import com.visualipcv.view.events.DragDropEventListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class NodeSlotView extends JComponent {
    private static final int STROKE_THICKNESS = 3;

    private ArrayList<SlotConnection> connections = new ArrayList<>();
    private NodeView node;
    private NodeSlotType slotType;

    private DragDropEventListener dragDropEventListener;

    public NodeSlotView(NodeView node, NodeSlotType type) {
        this.slotType = type;
        this.node = node;
        setBackground(new Color(50, 0, 0, 255));
        setForeground(new Color(200, 0, 0, 255));
        setTransferHandler(new SlotDragHandler());
        addMouseListener(new DragMouseAdapter());
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D)graphics;
        graphics2D.setColor(getBackground());
        graphics2D.fillOval(0, 0, getWidth(), getHeight());
        graphics2D.setColor(getForeground());
        graphics2D.setStroke(new BasicStroke(STROKE_THICKNESS));
        graphics2D.drawOval(
                STROKE_THICKNESS / 2,
                STROKE_THICKNESS / 2,
                getWidth() - STROKE_THICKNESS + 1,
                getHeight() - STROKE_THICKNESS + 1);
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
