package com.visualipcv.core;

import com.visualipcv.core.events.RefreshEventListener;

import java.util.ArrayList;
import java.util.List;

public class Refreshable {
    private List<RefreshEventListener> eventListenerList = new ArrayList<>();

    public void addListener(RefreshEventListener eventListener) {
        eventListenerList.add(eventListener);
    }

    public void removeListener(RefreshEventListener eventListener) {
        eventListenerList.remove(eventListener);
    }

    public void refresh() {
        for(RefreshEventListener listener : eventListenerList) {
            listener.refresh();
        }
    }
}
