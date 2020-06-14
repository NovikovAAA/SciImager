package com.visualipcv.utils;

import org.apache.commons.lang3.SystemUtils;
import org.opencv.core.Core;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

public class LinkUtils {
    public static void linkNativeLibraries() {
        try {
            addDir("./ext");
            addDir("/Users/artemnovikov/Documents/Applications/scilab-branch-6.1.app/Contents/MacOS/lib/scilab");
            addDir("/Users/artemnovikov/Documents/Applications/scilab-branch-6.1.app/Contents/MacOS/lib/thirdparty");
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.loadLibrary("VisualIPCV");
        System.loadLibrary("VisualIPCVJava");

        if(SystemUtils.OS_NAME.toLowerCase().contains("win")) {
            System.loadLibrary("opencv_world420d");
        } else {
            System.loadLibrary("opencv_java430");
        }
    }

    public static void addDir(String s) throws IOException {
        try {
            Field field = ClassLoader.class.getDeclaredField("usr_paths");
            field.setAccessible(true);
            String[] paths = (String[])field.get(null);
            for (int i = 0; i < paths.length; i++) {
                if (s.equals(paths[i])) {
                    return;
                }
            }
            String[] tmp = new String[paths.length+1];
            System.arraycopy(paths,0,tmp,0,paths.length);
            tmp[paths.length] = s;
            field.set(null,tmp);
            System.setProperty("java.library.path", System.getProperty("java.library.path") + File.pathSeparator + s);
        } catch (IllegalAccessException e) {
            throw new IOException("Failed to get permissions to set library path");
        } catch (NoSuchFieldException e) {
            throw new IOException("Failed to get field handle to set library path");
        }
    }
}
