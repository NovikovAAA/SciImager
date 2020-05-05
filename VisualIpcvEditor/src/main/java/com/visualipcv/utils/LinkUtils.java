package com.visualipcv.utils;

import org.opencv.core.Core;

public class LinkUtils {
    public static void linkNativeLibraries() {
        if(!System.getProperty("os.name").contains("win")) {
            String libsPath = System.getProperty("user.dir") + "/ext/";
            String libExt = ".dylib";

            System.load(libsPath +"libopencv_java430" + libExt);
            System.load(libsPath +"libVisualIPCV" + libExt);
            System.load(libsPath +"libVisualIpcvJava" + libExt);
//            System.load("/Users/artemnovikov/Documents/Учеба/Диплом/Scilmager/VisualIpcvEditor/Plugins/libCoreLibrary.dylib");
        } else {
            System.loadLibrary("ext/opencv_world420d");
            System.loadLibrary("ext/opencv_java420");
            System.loadLibrary("ext/VisualIPCV");
            System.loadLibrary("ext/VisualIPCVJava");
            System.loadLibrary("ext/" + Core.NATIVE_LIBRARY_NAME);
        }
    }
}
