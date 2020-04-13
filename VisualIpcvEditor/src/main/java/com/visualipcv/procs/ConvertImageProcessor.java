package com.visualipcv.procs;

import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.OpenCvDataTypes;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorBuilder;
import com.visualipcv.core.ProcessorProperty;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

public class ConvertImageProcessor extends Processor {
    public ConvertImageProcessor() {
        super(new ProcessorBuilder()
            .setName("ConvertImage")
            .setModule("Core")
            .setCategory("Image")
            .addInputProperty(new ProcessorProperty("Image", DataType.IMAGE))
            .addInputProperty(new ProcessorProperty("Target", OpenCvDataTypes.CV_IMAGE_TYPE))
            .addOutputProperty(new ProcessorProperty("Result", DataType.IMAGE)));
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
