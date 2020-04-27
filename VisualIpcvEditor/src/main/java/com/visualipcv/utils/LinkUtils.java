package com.visualipcv.utils;

import org.opencv.core.Core;

public class LinkUtils {
    public static void linkNativeLibraries() {
        if(System.getProperty("os.name").contains("mac")) {
            System.load("/Users/artemnovikov/Documents/Учеба/Диплом/SciImager/VisualIpcvEditor/ext/libopencv_java420.dylib");
            System.load("/Users/artemnovikov/Documents/Учеба/Диплом/SciImager/VisualIpcvEditor/ext/libVisualIPCV.dylib");
            System.load("/Users/artemnovikov/Documents/Учеба/Диплом/SciImager/VisualIpcvEditor/ext/libVisualIpcvJava.dylib");
        } else {
            System.loadLibrary("ext/opencv_world420d");
            System.loadLibrary("ext/opencv_java420");
            System.loadLibrary("ext/VisualIPCV");
            System.loadLibrary("ext/VisualIPCVJava");
            System.loadLibrary("ext/" + Core.NATIVE_LIBRARY_NAME);
        }
    }
}
