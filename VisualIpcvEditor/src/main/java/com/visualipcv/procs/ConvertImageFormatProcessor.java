package com.visualipcv.procs;

import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.DataTypes;
import com.visualipcv.core.OpenCvDataTypes;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorBuilder;
import com.visualipcv.core.ProcessorProperty;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

public class ConvertImageFormatProcessor extends Processor {
    public ConvertImageFormatProcessor() {
        super(new ProcessorBuilder()
            .setName("ConvertImageFormat")
            .setModule("Core")
            .setCategory("Image")
            .addInputProperty(new ProcessorProperty("Image", DataTypes.IMAGE))
            .addInputProperty(new ProcessorProperty("Target", OpenCvDataTypes.CV_IMAGE_TYPE))
            .addOutputProperty(new ProcessorProperty("Result", DataTypes.IMAGE)));
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle state) {
        Mat image = inputs.read("Image");
        int cvType = inputs.read("Target");

        Mat result = new Mat();
        DataBundle outputs = new DataBundle();

        if(image.type() != cvType) {
            image.convertTo(result, cvType);
        } else {
            image.copyTo(result);
        }

        outputs.write("Result", result);
        return outputs;
    }
}
