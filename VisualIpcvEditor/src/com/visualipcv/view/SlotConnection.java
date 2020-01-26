package com.visualipcv.view;

import javax.swing.*;
import java.awt.*;

public class SlotConnection extends AbstractSlotConnection {
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
        setForeground(this.sourceSlot.getProperty().getType().getColor());
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

    @Override
    public void updateBounds() {
        int x0 = sourceSlot.getGraphRelativeX() + sourceSlot.getWidth() / 2;
        int x1 = targetSlot.getGraphRelativeX() + targetSlot.getWidth() / 2;
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
    public int getSourceX() {
        return sourceSlot.getGraphRelativeX() - getX() + sourceSlot.getWidth()  / 2;
    }

    @Override
    public int getSourceY() {
        return sourceSlot.getGraphRelativeY() - getY() + sourceSlot.getHeight() / 2;
    }

    @Override
    public int getTargetX() {
        return targetSlot.getGraphRelativeX() - getX() + targetSlot.getWidth() / 2;
    }

    @Override
    public int getTargetY() {
        return targetSlot.getGraphRelativeY() - getY() + targetSlot.getHeight() / 2;
    }
}
