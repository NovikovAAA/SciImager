package com.visualipcv;

import com.visualipcv.core.DataTypeLibrary;
import com.visualipcv.core.ProcessorLibrary;
import com.visualipcv.scripts.SciRunner;
import com.visualipcv.utils.LinkUtils;
import javafx.application.Application;

import java.io.IOException;

public class Starter {
    static {
        LinkUtils.linkNativeLibraries();
        SciRunner.load();
//        DataTypeLibrary.initDefaultTypes();
        ProcessorLibrary.getInstance();
    }

    public static void main(String[] args) throws IOException {
        Main.start(args);
    }
}
