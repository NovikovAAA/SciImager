package com.visualipcv.procs;

import org.opencv.videoio.VideoCapture;

import java.util.HashMap;

public class CameraCaptureHelper {
    private static HashMap<Integer, VideoCapture> capture = new HashMap<>();

    public static VideoCapture get(int index) {
        if(capture.containsKey(index)) {
            return capture.get(index);
        } else {
            VideoCapture capture = new VideoCapture(index);
            CameraCaptureHelper.capture.put(index, capture);
            return capture;
        }
    }
}
