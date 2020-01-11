package com.visualipcv.view;

import javax.swing.*;
import java.awt.*;

public class SlotConnection extends JComponent {
    private static final int BEZIER_OFFSET = 200;

    private NodeSlotView sourceSlot;
    private NodeSlotView targetSlot;

    public SlotConnection(NodeSlotView sourceSlot, NodeSlotView targetSlot) {
        this.sourceSlot = sourceSlot;
        this.targetSlot = targetSlot;

        if(this.sourceSlot.getType() != NodeSlotType.OUTPUT) {
            this.sourceSlot = targetSlot;
            this.targetSlot = sourceSlot;
        }

        targetSlot.connect(this);
        sourceSlot.connect(this);

        updateBounds();
        setForeground(Color.red);
    }

    public void remove() {
        targetSlot.disconnect(this);
        sourceSlot.disconnect(this);
    }

    public NodeSlotView getSourceSlot() {
        return sourceSlot;
    }

    public NodeSlotView getTargetSlot() {
        return targetSlot;
    }

    private Point bezierLine(float t, Point p0, Point p1, Point p2, Point p3) {
        float x0 = p0.x;
        float x1 = p1.x;
        float x2 = p2.x;
        float x3 = p3.x;
        float y0 = p0.y;
        float y1 = p1.y;
        float y2 = p2.y;
        float y3 = p3.y;

        float x = (1 - t) * (1 - t) * (1 - t) * x0 + 3 * t * (1 - t) * (1 - t) * x1 + 3 * t * t * (1 - t) * x2 + t * t * t * x3;
        float y = (1 - t) * (1 - t) * (1 - t) * y0 + 3 * t * (1 - t) * (1 - t) * y1 + 3 * t * t * (1 - t) * y2 + t * t * t * y3;
        return new Point((int)x, (int)y);
    }

    public void updateBounds() {
        int x0 = sourceSlot.getGraphRelativeX() + sourceSlot.getWidth() / 2;
        int x1 = targetSlot.getGraphRelativeX() + sourceSlot.getWidth() / 2;
        int y0 = sourceSlot.getGraphRelativeY() + sourceSlot.getHeight() / 2;
        int y1 = targetSlot.getGraphRelativeY() + targetSlot.getHeight() / 2;

        int xmin = Math.min(x0, x1) - BEZIER_OFFSET - 5;
        int xmax = Math.max(x0, x1) + BEZIER_OFFSET + 5;
        int ymin = Math.min(y0, y1) - 5;
        int ymax = Math.max(y0, y1) + 5;

        setLocation(new Point(xmin, ymin));
        setSize(new Dimension(xmax - xmin, ymax - ymin));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g2d = (Graphics2D)graphics;

        int diffX = targetSlot.getGraphRelativeX() - sourceSlot.getGraphRelativeX();
        int diffY = targetSlot.getGraphRelativeY() - sourceSlot.getGraphRelativeY();
        int dist = (int)Math.sqrt(diffX * diffX + diffY * diffY);
        int offset = Math.min(BEZIER_OFFSET, dist);

        Point p0 = new Point(
                sourceSlot.getGraphRelativeX() - getX() + sourceSlot.getWidth()  / 2,
                sourceSlot.getGraphRelativeY() - getY() + sourceSlot.getHeight() / 2);
        Point p1 = new Point(
                sourceSlot.getGraphRelativeX() - getX() + sourceSlot.getWidth() / 2 + offset,
                sourceSlot.getGraphRelativeY() - getY() + sourceSlot.getHeight() / 2);
        Point p2 = new Point(
                targetSlot.getGraphRelativeX() - getX() + targetSlot.getWidth() / 2 - offset,
                targetSlot.getGraphRelativeY() - getY() + targetSlot.getHeight() / 2);
        Point p3 = new Point(
                targetSlot.getGraphRelativeX() - getX() + targetSlot.getWidth() / 2,
                targetSlot.getGraphRelativeY() - getY() + targetSlot.getHeight() / 2);

        for(int i = 0; i < 32; i++) {
            float t0 = i * (1.0f / 32.0f);
            float t1 = (i + 1) * (1.0f / 32.0f);

            Point pSource = bezierLine(t0, p0, p1, p2, p3);
            Point pTarget = bezierLine(t1, p0, p1, p2, p3);
            g2d.setColor(getForeground());
            g2d.setStroke(new BasicStroke(3));
            g2d.drawLine(pSource.x, pSource.y, pTarget.x, pTarget.y);
        }
    }
}
