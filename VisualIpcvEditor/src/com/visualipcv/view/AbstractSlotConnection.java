package com.visualipcv.view;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractSlotConnection extends JComponent {
    protected static final int BEZIER_OFFSET = 200;

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

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g2d = (Graphics2D)graphics;

        int diffX = getTargetX() - getSourceX();
        int diffY = getTargetY() - getSourceY();
        int dist = (int)Math.sqrt(diffX * diffX + diffY * diffY);
        int offset = Math.min(BEZIER_OFFSET, dist);

        Point p0 = new Point(getSourceX(), getSourceY());
        Point p1 = new Point(getSourceX() + offset, getSourceY());
        Point p2 = new Point(getTargetX() - offset, getTargetY());
        Point p3 = new Point(getTargetX(), getTargetY());

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

    public abstract int getSourceX();
    public abstract int getSourceY();
    public abstract int getTargetX();
    public abstract int getTargetY();
    public abstract void updateBounds();
}
