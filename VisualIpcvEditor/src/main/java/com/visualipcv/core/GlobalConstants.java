package com.visualipcv.core;

import java.util.ArrayList;
import java.util.List;

public class GlobalConstants {
    public static List<String> IMAGE_FORMAT = new ArrayList<String>() {
        {
            add(".png");
            add(".jpeg");
            add(".jpg");
            add(".tiff");
        }
    };
}
