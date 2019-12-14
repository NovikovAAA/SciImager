package com.visualipcv.view;

import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;

public class DragListener extends MouseInputAdapter {
    Point location;
    MouseEvent pressed;

    @Override
    public void mousePressed(MouseEvent e) {
        pressed = e;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Component c = e.getComponent();
        location = c.getLocation();
        int deltaX = e.getX() - pressed.getX();
        int deltaY = e.getY() - pressed.getY();
        int x = location.x + deltaX;
        int y = location.y + deltaY;
        c.setLocation(x, y);
        dragged(deltaX, deltaY);
    }

    public void dragged(int deltaX, int deltaY) {

    }
}