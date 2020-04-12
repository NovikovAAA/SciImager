package com.visualipcv;

import com.visualipcv.events.ConsoleEventListener;
import com.visualipcv.scripts.SciRunner;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Console {
    private static List<ConsoleEventListener> eventListeners = new ArrayList<>();
    private static ByteArrayOutputStream byteStream;

    static {
        byteStream = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(byteStream);
        System.setOut(stream);
        System.setErr(stream);
    }

    public static String execute(String cmd, boolean showCmd) {
        SciRunner.execute(cmd);

        if(showCmd) {
            System.out.println(cmd);
            update();
        }

        return "";
    }

    public static void update() {
        for(ConsoleEventListener listener : eventListeners) {
            listener.onUpdate(new String(byteStream.toByteArray()));
        }
    }

    public static void addEventListener(ConsoleEventListener listener) {
        eventListeners.add(listener);
    }

    public static void removeEventListener(ConsoleEventListener listener) {
        eventListeners.remove(listener);
    }
}
