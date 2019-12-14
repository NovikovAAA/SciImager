package com.visualipcv.view;

import javax.swing.*;
import java.awt.*;

public class NodeSlotView extends JComponent {
    private boolean isConnected = false;

    public NodeSlotView() {
        setPreferredSize(new Dimension(50, 50));
        setBackground(new Color(50, 0, 0, 255));
        setForeground(new Color(200, 0, 0, 255));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D)graphics;
        graphics2D.setColor(getBackground());
        graphics2D.fillOval(0, 0, getWidth(), getHeight());
        graphics2D.setColor(getForeground());
        graphics2D.setStroke(new BasicStroke(5));
        graphics2D.drawOval(0, 0, getWidth(), getHeight());
    }
}
