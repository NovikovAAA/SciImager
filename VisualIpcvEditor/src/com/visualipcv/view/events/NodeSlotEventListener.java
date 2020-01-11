package com.visualipcv.view.events;

import com.visualipcv.NodeSlot;

public interface NodeSlotEventListener {
    void onConnected(NodeSlot source, NodeSlot target);
    void onDisconnected(NodeSlot source, NodeSlot target);
}
