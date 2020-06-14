package com.visualipcv.utils;

import org.apache.commons.lang3.SystemUtils;
import org.opencv.core.Core;

public class LinkUtils {
    public static void linkNativeLibraries() {
        if(SystemUtils.OS_NAME.toLowerCase().contains("win")) {
            System.loadLibrary("ext/opencv_world420d");
            System.loadLibrary("ext/opencv_java420");
            System.loadLibrary("ext/VisualIPCV");
            System.loadLibrary("ext/VisualIPCVJava");
            System.loadLibrary("ext/" + Core.NATIVE_LIBRARY_NAME);
        } else {
            System.loadLibrary("ext/opencv_world430");
            System.loadLibrary("ext/opencv_java430");
            System.loadLibrary("ext/VisualIPCV");
            System.loadLibrary("ext/VisualIPCVJava");
            System.loadLibrary("ext/" + Core.NATIVE_LIBRARY_NAME);
        }
    }
}
