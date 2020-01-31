package com.visualipcv.view.events;

import com.visualipcv.view.AbstractNodeView;
import com.visualipcv.view.NodeView;

public interface NodeEventListener {
    void onMove(AbstractNodeView nodeView, int deltaX, int deltaY);
    void onDelete(AbstractNodeView nodeView);
}
