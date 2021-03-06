package com.visualipcv;

import com.sun.javafx.runtime.VersionInfo;
import com.visualipcv.core.Converter;
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
        Converter.registerDefaultConverters();
    }

    public static void main(String[] args) throws IOException {
        Console.write("Java version: " + System.getProperty("java.version"));
        Console.write("JavaFX version: " + VersionInfo.getRuntimeVersion());
        Main.start(args);
    }
}
