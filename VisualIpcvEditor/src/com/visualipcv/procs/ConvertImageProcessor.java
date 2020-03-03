package com.visualipcv.procs;

import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.OpenCvDataTypes;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorProperty;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

public class ConvertImageProcessor extends Processor {
    public ConvertImageProcessor() {
        super("ConvertImage", "Core", "Image",
                new ArrayList<ProcessorProperty>() {
                    {
                        add(new ProcessorProperty("Image", DataType.IMAGE));
                        add(new ProcessorProperty("Target", OpenCvDataTypes.CV_IMAGE_TYPE));
                    }
                },
                new ArrayList<ProcessorProperty>() {
                    {
                        add(new ProcessorProperty("Result", DataType.IMAGE));
                    }
                });
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle state) {
        Mat image = inputs.read("Image");
        int cvType = inputs.read("Target");

        Mat result = new Mat();
        DataBundle outputs = new DataBundle();
        image.convertTo(result, cvType);
        outputs.write("Result", result);

        return outputs;
    }
}
