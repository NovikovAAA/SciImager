package com.visualipcv.utils;

import org.opencv.core.Core;

public class LinkUtils {
    public static void linkNativeLibraries() {
        System.loadLibrary("ext/opencv_world420d");
        System.loadLibrary("ext/VisualIPCV");
        System.loadLibrary("ext/VisualIpcvJava");
        System.loadLibrary("ext/" + Core.NATIVE_LIBRARY_NAME);
    }
}
