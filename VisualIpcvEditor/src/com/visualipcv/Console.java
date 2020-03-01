package com.visualipcv;

import com.visualipcv.events.ConsoleEventListener;
import com.visualipcv.scripts.SciRunner;

import java.util.ArrayList;
import java.util.List;

public class Console {
    private static List<ConsoleEventListener> eventListeners = new ArrayList<>();

    public static String write(String cmd, boolean showCmd) {
        SciRunner.execute(cmd);

        if(showCmd)
            output(cmd);

        return "";
    }

    public static void output(String text) {
        for(ConsoleEventListener listener : eventListeners) {
            listener.onRecordAdded(text);
            listener.onResponse(text);
        }
    }

    public static void addEventListener(ConsoleEventListener listener) {
        eventListeners.add(listener);
    }

    public static void removeEventListener(ConsoleEventListener listener) {
        eventListeners.remove(listener);
    }
}
