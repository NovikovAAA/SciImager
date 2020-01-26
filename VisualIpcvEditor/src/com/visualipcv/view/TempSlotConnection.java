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
        setLocation(new Point(
                sourceSlot.getNode().getGraph().getActiveArea().x,
                sourceSlot.getNode().getGraph().getActiveArea().y));
        setSize(new Dimension(
                sourceSlot.getNode().getGraph().getActiveArea().width,
                sourceSlot.getNode().getGraph().getActiveArea().height));
    }

    @Override
    public int getSourceX() {
        if(sourceSlot.getType() == NodeSlotType.OUTPUT)
            return sourceSlot.getGraphRelativeX() - getX() + sourceSlot.getWidth()  / 2;
        else
            return sourceSlot.getNode().getGraph().getMousePositionX() - getX();
    }

    @Override
    public int getSourceY() {
        if(sourceSlot.getType() == NodeSlotType.OUTPUT)
            return sourceSlot.getGraphRelativeY() - getY() + sourceSlot.getHeight() / 2;
        else
            return sourceSlot.getNode().getGraph().getMousePositionY() - getY();
    }

    @Override
    public int getTargetX() {
        if(sourceSlot.getType() == NodeSlotType.INPUT)
            return sourceSlot.getGraphRelativeX() - getX() + sourceSlot.getWidth()  / 2;
        else
            return sourceSlot.getNode().getGraph().getMousePositionX() - getX();
    }

    @Override
    public int getTargetY() {
        if(sourceSlot.getType() == NodeSlotType.INPUT)
            return sourceSlot.getGraphRelativeY() - getY() + sourceSlot.getHeight() / 2;
        else
            return sourceSlot.getNode().getGraph().getMousePositionY() - getY();
    }
}
