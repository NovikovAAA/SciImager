package com.visualipcv.view;

import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;

public class NodeMouseEventListener extends MouseInputAdapter {
    Point location;
    MouseEvent pressed;

    @Override
    public void mousePressed(MouseEvent e) {
        pressed = e;
        e.getComponent().getParent().setComponentZOrder(e.getComponent(), 0);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int deltaX = e.getLocationOnScreen().x - pressed.getLocationOnScreen().x;
        int deltaY = e.getLocationOnScreen().y - pressed.getLocationOnScreen().y;
        dragged(deltaX, deltaY);
        pressed = e;
    }

    public void dragged(int deltaX, int deltaY) {

    }
}