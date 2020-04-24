package com.visualipcv;

import com.visualipcv.core.DataTypeLibrary;
import com.visualipcv.core.ProcessorLibrary;
import com.visualipcv.core.ReflectedProcessorGenerator;
import com.visualipcv.scripts.SciRunner;
import com.visualipcv.utils.LinkUtils;

import java.io.IOException;

public class Starter {
    static {
        LinkUtils.linkNativeLibraries();
        SciRunner.load();
        DataTypeLibrary.init();
        ProcessorLibrary.getInstance();
        ReflectedProcessorGenerator.loadReflected();
    }

    public static void main(String[] args) throws IOException {
        Main.start(args);
    }
}
