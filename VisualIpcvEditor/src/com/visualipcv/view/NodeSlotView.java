package com.visualipcv.view;

import javax.swing.*;
import java.awt.*;

public class NodeSlotView extends JComponent {
    private static final int STROKE_THICKNESS = 3;

    private boolean isConnected = false;

    public NodeSlotView() {
        setBackground(new Color(50, 0, 0, 255));
        setForeground(new Color(200, 0, 0, 255));
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
}
