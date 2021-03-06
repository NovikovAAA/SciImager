package com.visualipcv;

import com.visualipcv.core.events.ConsoleEventListener;
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
        try {
            SciRunner.execute(cmd);

            if(showCmd) {
                System.out.println(cmd);
                update();
            }
        } catch (Exception e) {
            Console.error(e.getMessage());
        }

        return "";
    }

    public static void write(String msg) {
        System.out.println(msg);
        update();
    }

    public static void error(String msg) {
        System.err.println(msg);
        update();
    }

    public static void error(Throwable e) {
        //Console.error(e.getMessage());
        e.printStackTrace();
        update();
    }

    public static void clear() {
        byteStream.reset();
        update();
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
