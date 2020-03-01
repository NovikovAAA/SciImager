package com.visualipcv.scripts;

import org.scilab.modules.javasci.JavasciException;
import org.scilab.modules.javasci.Scilab;
import org.scilab.modules.types.*;

public class SciRunner {
    private static Scilab scilab;

    public static void load() {
        try {
            scilab = new Scilab(true);
        } catch(JavasciException.InitializationException e) {
            e.printStackTrace();
        }

        try {
            scilab.open();
        } catch(JavasciException e) {
            e.printStackTrace();
        }

        SciConverters.load();
    }

    public static void set(String name, ScilabType value) {
        try {
            scilab.put(name, value);
        } catch(JavasciException e) {
            e.printStackTrace();
        }
    }

    public static ScilabType get(String name) {
        try {
            return scilab.get(name);
        } catch(JavasciException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void execute(String code) {
        scilab.exec(code);
    }
}
