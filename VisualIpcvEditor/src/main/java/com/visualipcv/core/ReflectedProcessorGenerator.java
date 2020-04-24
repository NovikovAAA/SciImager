package com.visualipcv.core;

import com.visualipcv.procs.ReflectedProcessor;
import org.opencv.core.Mat;

import java.lang.reflect.Method;

public class ReflectedProcessorGenerator {
    public static void loadReflected() {
        reflect(Mat.class, "channels");
        reflect(Mat.class, "total");
    }

    private static void reflect(Class<?> clazz, String method) {
        try {
            ProcessorLibrary.addProcessor(new ReflectedProcessor(clazz.getMethod(method)));
        } catch (Exception e) {

        }
    }
}
