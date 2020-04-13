package com.visualipcv.utils;

import org.opencv.core.Core;

public class LinkUtils {
    public static void linkNativeLibraries() {
        System.load("/Users/artemnovikov/Documents/Учеба/Диплом/SciImager/VisualIpcvEditor/ext/libopencv_java420.dylib");
        System.load("/Users/artemnovikov/Documents/Учеба/Диплом/SciImager/VisualIpcvEditor/ext/libVisualIPCV.dylib");
        System.load("/Users/artemnovikov/Documents/Учеба/Диплом/SciImager/VisualIpcvEditor/ext/libVisualIpcvJava.dylib");
//        System.loadLibrary("ext/" + Core.NATIVE_LIBRARY_NAME);
    }
}
