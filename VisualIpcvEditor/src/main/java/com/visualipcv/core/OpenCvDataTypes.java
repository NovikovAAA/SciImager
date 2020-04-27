package com.visualipcv.core;

import com.visualipcv.core.annotations.RegisterDataType;
import com.visualipcv.core.dataconstraints.EnumConstraint;
import org.opencv.core.CvType;

import java.awt.*;

public class OpenCvDataTypes {
    @RegisterDataType
    public static final DataType CV_IMAGE_TYPE = new DataType("CvType", Color.BLUE, Integer.class, new EnumConstraint(
            new EnumConstraint.Value(CvType.CV_8U, "CV_8U"),
            new EnumConstraint.Value(CvType.CV_8S, "CV_8S"),
            new EnumConstraint.Value(CvType.CV_16U, "CV_16U"),
            new EnumConstraint.Value(CvType.CV_16S, "CV_16S"),
            new EnumConstraint.Value(CvType.CV_32S, "CV_32S"),
            new EnumConstraint.Value(CvType.CV_32F, "CV_32F"),
            new EnumConstraint.Value(CvType.CV_64F, "CV_64F"),
            new EnumConstraint.Value(CvType.CV_16F, "CV_16F"),
            new EnumConstraint.Value(CvType.CV_8UC1, "CV_8UC1"),
            new EnumConstraint.Value(CvType.CV_8UC2, "CV_8UC2"),
            new EnumConstraint.Value(CvType.CV_8UC3, "CV_8UC3"),
            new EnumConstraint.Value(CvType.CV_8UC4, "CV_8UC4"),
            new EnumConstraint.Value(CvType.CV_8SC1, "CV_8SC1"),
            new EnumConstraint.Value(CvType.CV_8SC2, "CV_8SC2"),
            new EnumConstraint.Value(CvType.CV_8SC3, "CV_8SC3"),
            new EnumConstraint.Value(CvType.CV_8SC4, "CV_8SC4"),
            new EnumConstraint.Value(CvType.CV_16UC1, "CV_16UC1"),
            new EnumConstraint.Value(CvType.CV_16UC2, "CV_16UC2"),
            new EnumConstraint.Value(CvType.CV_16UC3, "CV_16UC3"),
            new EnumConstraint.Value(CvType.CV_16UC4, "CV_16UC4"),
            new EnumConstraint.Value(CvType.CV_16SC1, "CV_16SC1"),
            new EnumConstraint.Value(CvType.CV_16SC2, "CV_16SC2"),
            new EnumConstraint.Value(CvType.CV_16SC3, "CV_16SC3"),
            new EnumConstraint.Value(CvType.CV_16SC4, "CV_16SC4"),
            new EnumConstraint.Value(CvType.CV_32SC1, "CV_32SC1"),
            new EnumConstraint.Value(CvType.CV_32SC2, "CV_32SC2"),
            new EnumConstraint.Value(CvType.CV_32SC3, "CV_32SC3"),
            new EnumConstraint.Value(CvType.CV_32SC4, "CV_32SC4"),
            new EnumConstraint.Value(CvType.CV_32FC1, "CV_32FC1"),
            new EnumConstraint.Value(CvType.CV_32FC2, "CV_32FC2"),
            new EnumConstraint.Value(CvType.CV_32FC3, "CV_32FC3"),
            new EnumConstraint.Value(CvType.CV_32FC4, "CV_32FC4"),
            new EnumConstraint.Value(CvType.CV_64FC1, "CV_64FC1"),
            new EnumConstraint.Value(CvType.CV_64FC2, "CV_64FC2"),
            new EnumConstraint.Value(CvType.CV_64FC3, "CV_64FC3"),
            new EnumConstraint.Value(CvType.CV_64FC4, "CV_64FC4"),
            new EnumConstraint.Value(CvType.CV_16FC1, "CV_16FC1"),
            new EnumConstraint.Value(CvType.CV_16FC2, "CV_16FC2"),
            new EnumConstraint.Value(CvType.CV_16FC3, "CV_16FC3"),
            new EnumConstraint.Value(CvType.CV_16FC4, "CV_16FC4")
    )) {
        @Override
        public Object getDefaultValue() {
            return CvType.CV_8UC3;
        }
    };
}
