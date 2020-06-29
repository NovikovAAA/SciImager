package com.visualipcv.scripts;

import com.visualipcv.Console;
import com.visualipcv.core.CommonException;
import org.apache.commons.lang3.StringUtils;
import org.scilab.modules.javasci.JavasciException;
import org.scilab.modules.javasci.Scilab;
import org.scilab.modules.types.*;

public class SciRunner {
    private static Scilab scilab;
    public static void load() {
        try {
            System.out.println(System.getProperty("SCI"));
            scilab = new Scilab(true);
        } catch(JavasciException.InitializationException e) {
            Console.error(e);
        }

        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }

        try {
            scilab.open();
        } catch(JavasciException e) {
            Console.error(e);
            throw new RuntimeException(e);
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

    private static void repairScilab() {
        //scilab.exec("clear;");
        /*try {

        } catch (JavasciException e) {
            Console.error(e.getMessage());
        }*/
    }

    public static void execute(String code) {
        String safeCode = "try\n" + code + "\ncatch\n[error_message,error_number]=lasterror(%t);\nend\n";

        try {
            scilab.put("error_message", new ScilabString(""));
            scilab.execException(safeCode);
            String errMessage = ((ScilabString)scilab.get("error_message")).getData()[0][0];

            if(!StringUtils.isEmpty(errMessage))
                throw new CommonException(errMessage);

        } catch (JavasciException e) {
            repairScilab();
            throw new CommonException(e.getMessage());
        }
    }
}
