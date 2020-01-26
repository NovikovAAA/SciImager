package com.visualipcv.view;

import javax.swing.*;
import java.awt.*;

public class TempSlotConnection extends AbstractSlotConnection {
    private NodeSlotView sourceSlot;

    public TempSlotConnection(NodeSlotView sourceSlot) {
        this.sourceSlot = sourceSlot;

        updateBounds();
        setForeground(this.sourceSlot.getProperty().getType().getColor());
    }

    @Override
    public void updateBounds() {
        /*int x0 = sourceSlot.getGraphRelativeX() + sourceSlot.getWidth() / 2;
        int x1 = sourceSlot.getNode().getGraph().getMousePositionX();
        int y0 = sourceSlot.getGraphRelativeY() + sourceSlot.getHeight() / 2;
        int y1 = sourceSlot.getNode().getGraph().getMousePositionY();

        int xmin = Math.min(x0, x1) - BEZIER_OFFSET - 5;
        int xmax = Math.max(x0, x1) + BEZIER_OFFSET + 5;
        int ymin = Math.min(y0, y1) - 5;
        int ymax = Math.max(y0, y1) + 5;

        setLocation(new Point(xmin, ymin));
        setSize(new Dimension(xmax - xmin, ymax - ymin));*/

        setLocation(new Point(
                sourceSlot.getNode().getGraph().getActiveArea().x,
                sourceSlot.getNode().getGraph().getActiveArea().y));
        setSize(new Dimension(
                sourceSlot.getNode().getGraph().getActiveArea().width,
                sourceSlot.getNode().getGraph().getActiveArea().height));
    }

    @Override
    public int getSourceX() {
        return sourceSlot.getGraphRelativeX() - getX() + sourceSlot.getWidth()  / 2;
    }

    @Override
    public int getSourceY() {
        return sourceSlot.getGraphRelativeY() - getY() + sourceSlot.getHeight() / 2;
    }

    @Override
    public int getTargetX() {
        return sourceSlot.getNode().getGraph().getMousePositionX() - getX();
    }

    @Override
    public int getTargetY() {
        return sourceSlot.getNode().getGraph().getMousePositionY() - getY();
    }
}
