package com.visualipcv.view.events;

import com.visualipcv.view.NodeView;

public interface NodeEventListener {
    void onMove(NodeView nodeView, int deltaX, int deltaY);
}
