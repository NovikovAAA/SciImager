package com.visualipcv.view.events;

import java.awt.*;

public interface DragDropEventListener {
    void onDrop(Object payload, Point location);
    void onBeginDrag(Object payload);
}
