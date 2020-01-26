package com.visualipcv.view;

import com.visualipcv.utils.LocationUtils;
import com.visualipcv.view.dragdrop.ProcessorDragHandler;
import com.visualipcv.view.events.DragDropEventListener;

import javax.swing.*;
import java.awt.*;
import java.awt.dnd.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.logging.Logger;

public class GraphView extends JPanel {
    private static final int GRAPH_WIDTH = 65537;
    private static final int GRAPH_HEIGHT = 65537;

    private Color backgroundColor = new Color(40, 40, 40, 255);
    private Color lineColor = Color.darkGray;
    private int cellSize = 50;
    private int offsetX = -GRAPH_WIDTH / 2;
    private int offsetY = -GRAPH_HEIGHT / 2;

    private int mousePositionX;
    private int mousePositionY;

    private Rectangle activeArea;

    private JPanel internalPanel;
    private TempSlotConnection tempSlotConnection;
    private ArrayList<NodeView> nodes = new ArrayList<NodeView>();

    private DragDropEventListener dropListener;

    public GraphView() {
        setLayout(null);
        internalPanel = new JPanel(null);
        internalPanel.setLocation(offsetX, offsetY);
        internalPanel.setSize(GRAPH_WIDTH, GRAPH_HEIGHT);
        internalPanel.setOpaque(false);
        super.add(internalPanel);

        DragListener drag = new DragListener() {
            @Override
            public void dragged(int deltaX, int deltaY) {
                offsetX += deltaX;
                offsetY += deltaY;
                repaint();
                revalidate();
                updateActiveArea();
            }
            @Override
            public void mouseMoved(MouseEvent event) {
                super.mouseMoved(event);
                mousePositionX = event.getX();
                mousePositionY = event.getY();
            }
            @Override
            public void mousePressed(MouseEvent event) {
                super.mousePressed(event);
                mousePositionX = event.getX();
                mousePositionY = event.getY();
            }
        };

        internalPanel.addMouseListener(drag);
        internalPanel.addMouseMotionListener(drag);
        setTransferHandler(new ProcessorDragHandler());

        DragSource.getDefaultDragSource().addDragSourceMotionListener(new DragSourceMotionListener() {
            @Override
            public void dragMouseMoved(DragSourceDragEvent dsde) {
                updateActiveArea();
                Point p = LocationUtils.screenToComponent(dsde.getLocation(), internalPanel);
                mousePositionX = (int)p.getX();
                mousePositionY = (int)p.getY();
                repaintTempConnection();
            }
        });

        DragSource.getDefaultDragSource().addDragSourceListener(new DragSourceListener() {
            @Override
            public void dragEnter(DragSourceDragEvent dsde) {

            }

            @Override
            public void dragOver(DragSourceDragEvent dsde) {

            }

            @Override
            public void dropActionChanged(DragSourceDragEvent dsde) {

            }

            @Override
            public void dragExit(DragSourceEvent dse) {

            }

            @Override
            public void dragDropEnd(DragSourceDropEvent dsde) {
                removeTempSlotConnection();
            }
        });
    }

    private void updateActiveArea() {
        activeArea = new Rectangle(-offsetX, -offsetY, getWidth(), getHeight());
    }

    public Point toGraphCoords(Point p) {
        int x = p.x - offsetX;
        int y = p.y - offsetY;
        return new Point(x, y);
    }

    public Point toRealCoords(Point p) {
        int x = p.x + offsetX;
        int y = p.y + offsetY;
        return new Point(x, y);
    }

    @Override
    public Component add(Component component) {
        Component c = internalPanel.add(component);

        if(c instanceof NodeView) {
            nodes.add((NodeView)c);
        }

        repaint();
        revalidate();

        if(component instanceof TempSlotConnection) {
            ((TempSlotConnection)component).updateBounds();
            tempSlotConnection = (TempSlotConnection)component;
        }

        return c;
    }

    @Override
    public void remove(Component component) {
        if(component instanceof NodeView) {
            nodes.remove((NodeView)component);
        }

        if(component instanceof  TempSlotConnection) {
            tempSlotConnection = null;
        }

        internalPanel.remove(component);
        repaint();
        revalidate();
    }

    public void removeTempSlotConnection() {
        if(tempSlotConnection != null) {
            remove(tempSlotConnection);
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D)graphics;
        graphics2D.setColor(backgroundColor);
        graphics2D.fillRect(0, 0, getWidth(), getHeight());

        Point min = toGraphCoords(new Point(0, 0));
        Point max = toGraphCoords(new Point(getWidth(), getHeight()));

        min.x /= cellSize;
        min.y /= cellSize;
        max.x /= cellSize;
        max.y /= cellSize;

        graphics2D.setColor(lineColor);

        for(int i = min.x; i <= max.x; i++) {
            Point p = toRealCoords(new Point(i * cellSize, 0));
            graphics2D.drawLine(p.x, 0, p.x, getHeight());
        }

        for(int i = min.y; i <= max.y; i++) {
            Point p = toRealCoords(new Point(0, i * cellSize));
            graphics2D.drawLine(0, p.y, getWidth(), p.y);
        }
    }

    public int getGraphWidth() {
        return GRAPH_WIDTH;
    }

    public int getGraphHeight() {
        return GRAPH_HEIGHT;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public int getMousePositionX() {
        return mousePositionX;
    }

    public int getMousePositionY() {
        return mousePositionY;
    }

    public JPanel getInternalPanel() {
        return internalPanel;
    }

    public Rectangle getActiveArea() {
        return activeArea;
    }

    public void setDropListener(DragDropEventListener listener) {
        dropListener = listener;
    }

    public void onDrop(Object payload, Point location) {
        if(dropListener == null) {
            return;
        }
        dropListener.onDrop(payload, location);
    }

    public void repaintTempConnection() {
        if(tempSlotConnection != null) {
            tempSlotConnection.updateBounds();
            tempSlotConnection.repaint();
        }
    }
}
