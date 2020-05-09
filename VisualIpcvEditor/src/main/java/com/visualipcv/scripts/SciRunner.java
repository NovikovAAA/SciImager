package com.visualipcv.scripts;

import com.visualipcv.Console;
import org.scilab.modules.javasci.JavasciException;
import org.scilab.modules.javasci.Scilab;
import org.scilab.modules.types.*;

public class SciRunner {
    private static Scilab scilab;

    public static void load() {
        try {
            scilab = new Scilab(true);
        } catch(JavasciException.InitializationException e) {
            Console.error(e);
        }

        try {
            scilab.open();
        } catch(JavasciException e) {
            Console.error(e);
        }

        SciConverters.load();
    }

    public static void set(String name, ScilabType value) {
        try {
            scilab.put(name, value);
        } catch(JavasciException e) {
            Console.error(e);
        }
    }

    public static ScilabType get(String name) {
        try {
            return scilab.get(name);
        } catch (Exception e) {
            return null;
        }
    }

    public static void execute(String code) {
        scilab.exec(code);

    }
}
