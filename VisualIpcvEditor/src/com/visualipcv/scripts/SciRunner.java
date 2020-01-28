package com.visualipcv.scripts;

import org.scilab.modules.javasci.JavasciException;
import org.scilab.modules.javasci.Scilab;
import org.scilab.modules.types.*;

public class SciRunner {
    private Scilab scilab;

    public SciRunner() throws JavasciException.InitializationException {
        scilab = new Scilab(true);

        try {
            scilab.open();
        } catch(JavasciException e) {
            e.printStackTrace();
        }
    }

    public void set(String name, ScilabType value) {
        try {
            scilab.put(name, value);
        } catch(JavasciException e) {
            e.printStackTrace();
        }
    }

    public ScilabType get(String name) {
        try {
            return scilab.get(name);
        } catch(JavasciException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void execute(String code) {
        scilab.exec(code);
    }
}
