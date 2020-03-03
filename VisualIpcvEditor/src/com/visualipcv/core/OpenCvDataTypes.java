package com.visualipcv.core;

import com.visualipcv.core.dataconstraints.EnumConstraint;
import org.opencv.core.CvType;

import java.awt.*;

public class OpenCvDataTypes {
    public static final DataType CV_IMAGE_TYPE = new DataType("CvType", Color.BLUE, new EnumConstraint(
            CvType.CV_8U,
            CvType.CV_8S,
            CvType.CV_16U,
            CvType.CV_16S,
            CvType.CV_32S,
            CvType.CV_32F,
            CvType.CV_64F,
            CvType.CV_16F,
            CvType.CV_8UC1, CvType.CV_8UC2, CvType.CV_8UC3, CvType.CV_8UC4,
            CvType.CV_8SC1, CvType.CV_8SC2, CvType.CV_8SC3, CvType.CV_8SC4,
            CvType.CV_16UC1, CvType.CV_16UC2, CvType.CV_16UC3, CvType.CV_16UC4,
            CvType.CV_16SC1, CvType.CV_16SC2, CvType.CV_16SC3, CvType.CV_16SC4,
            CvType.CV_32SC1, CvType.CV_32SC2, CvType.CV_32SC3, CvType.CV_32SC4,
            CvType.CV_32FC1, CvType.CV_32FC2, CvType.CV_32FC3, CvType.CV_32FC4,
            CvType.CV_64FC1, CvType.CV_64FC2, CvType.CV_64FC3, CvType.CV_64FC4,
            CvType.CV_16FC1, CvType.CV_16FC2, CvType.CV_16FC3, CvType.CV_16FC4
    )) {
        @Override
        public Object getDefaultValue() {
            return CvType.CV_8UC3;
        }
    };
}
