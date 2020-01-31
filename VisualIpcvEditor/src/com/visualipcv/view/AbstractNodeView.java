package com.visualipcv.view;

import com.visualipcv.view.events.NodeEventListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

public abstract class AbstractNodeView extends JPanel {
    private boolean isSelected;
    private GraphView graph;

    private NodeEventListener nodeEventListener;

    public AbstractNodeView(GraphView graph) {
        this.graph = graph;

        NodeMouseEventListener drag = new NodeMouseEventListener() {
            @Override
            public void mousePressed(MouseEvent event) {
                super.mousePressed(event);
                if((event.getModifiers() & ActionEvent.CTRL_MASK) == 0 && !isSelected()) {
                    getGraph().clearSelection();
                }
                setSelected(true);
                requestFocus();
            }
            @Override
            public void dragged(int deltaX, int deltaY) {
                getGraph().dragSelectedNodes(deltaX, deltaY);
            }
        };

        this.addMouseListener(drag);
        this.addMouseMotionListener(drag);
        this.setSelected(false);
        this.setFocusable(true);

        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar() == KeyEvent.VK_DELETE) {
                    getGraph().deleteSelectedNodes();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    public GraphView getGraph() {
        return graph;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    public void setNodeEventListener(NodeEventListener listener) {
        nodeEventListener = listener;
    }

    @Override
    public void setLocation(int x, int y) {
        int oldX = getLocation().x;
        int oldY = getLocation().y;
        super.setLocation(x, y);
        nodeEventListener.onMove(this, x - oldX, y - oldY);
    }

    @Override
    public void setLocation(Point p) {
        int oldX = getLocation().x;
        int oldY = getLocation().y;
        super.setLocation(p.x, p.y);
        nodeEventListener.onMove(this, p.x - oldX, p.y - oldY);
    }

    public void onDelete() {
        nodeEventListener.onDelete(this);
    }
}
