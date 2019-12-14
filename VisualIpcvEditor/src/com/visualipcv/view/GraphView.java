package com.visualipcv.view;

import com.visualipcv.Processor;

import javax.swing.*;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

public class GraphView extends JPanel implements Transferable {
    private static final int GRAPH_WIDTH = 65537;
    private static final int GRAPH_HEIGHT = 65537;

    private Color backgroundColor = new Color(40, 40, 40, 255);
    private Color lineColor = Color.darkGray;
    private int cellSize = 50;
    private int offsetX = -GRAPH_WIDTH / 2;
    private int offsetY = -GRAPH_HEIGHT / 2;

    private JPanel internalPanel;

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
            }
        };

        internalPanel.addMouseListener(drag);
        internalPanel.addMouseMotionListener(drag);
        setTransferHandler(new ProcessorDragHandler());
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
        repaint();
        revalidate();
        return c;
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

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return null;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return false;
    }

    @Override
    public Object getTransferData(DataFlavor flavor) {
        return null;
    }
}
