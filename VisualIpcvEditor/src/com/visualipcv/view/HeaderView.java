package com.visualipcv.view;

import javax.swing.*;
import java.awt.*;

public class HeaderView extends JLabel {
    private int radius = 10;

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D)graphics;

        if(this.isOpaque()) {
            graphics2D.setColor(getBackground());
            graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        }

        graphics2D.setColor(getForeground());
        graphics2D.drawString(getText(), 10, getHeight() - 5);
    }

    public void setRadius(int radius) {
        this.radius = radius;
        repaint();
    }

    public int getRadius() {
        return radius;
    }
}
