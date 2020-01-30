package com.visualipcv.utils;

public class LinkUtils {
    public static void linkNativeLibraries() {
        System.loadLibrary("ext/opencv_world420d");
        System.loadLibrary("ext/VisualIPCV");
        System.loadLibrary("ext/VisualIpcvJava");
    }
}
