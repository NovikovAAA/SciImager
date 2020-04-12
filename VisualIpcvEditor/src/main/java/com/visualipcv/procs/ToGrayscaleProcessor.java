package com.visualipcv.procs;

import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorBuilder;
import com.visualipcv.core.ProcessorProperty;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class ToGrayscaleProcessor extends Processor {
    public ToGrayscaleProcessor() {
        super(new ProcessorBuilder()
            .setName("ToGrayscale")
            .setCategory("Image")
            .setModule("Core")
            .addInputProperty(new ProcessorProperty("Image", DataType.IMAGE))
            .addOutputProperty(new ProcessorProperty("Result", DataType.IMAGE)));
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle state) {
        DataBundle result = new DataBundle();
        Mat image = inputs.read("Image");
        Mat converted = new Mat();
        Imgproc.cvtColor(image, converted, Imgproc.COLOR_RGB2GRAY);
        result.write("Result", converted);
        return result;
    }
}
