package com.visualipcv.procs;

import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorBuilder;
import com.visualipcv.core.ProcessorProperty;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class ThresholdProcessor extends Processor {
    public ThresholdProcessor() {
        super(new ProcessorBuilder()
            .setName("Threshold")
            .setModule("Core")
            .setCategory("Image")
            .addInputProperty(new ProcessorProperty("Image", DataType.IMAGE))
            .addInputProperty(new ProcessorProperty("Value", DataType.DOUBLE))
            .addInputProperty(new ProcessorProperty("Max", DataType.DOUBLE))
            .addOutputProperty(new ProcessorProperty("Result", DataType.IMAGE)));
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle state) {
        DataBundle result = new DataBundle();
        Mat image = inputs.read("Image");
        Mat out = new Mat();
        Imgproc.threshold(image, out, inputs.read("Value"), inputs.read("Max"), Imgproc.THRESH_BINARY);
        result.write("Result", out);
        return result;
    }
}
