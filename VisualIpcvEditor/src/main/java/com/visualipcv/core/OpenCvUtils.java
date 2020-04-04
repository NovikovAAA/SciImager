package com.visualipcv.core;

import org.opencv.core.CvType;

public class OpenCvUtils {
    public static boolean isUChar(int type) {
        return (type & (1 << 3 - 1)) == CvType.CV_8U;
    }
}
