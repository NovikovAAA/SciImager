package com.visualipcv.procs;

import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorBuilder;
import com.visualipcv.core.ProcessorProperty;
import org.opencv.core.Mat;
import org.opencv.video.BackgroundSubtractorMOG2;
import org.opencv.video.Video;

import java.util.ArrayList;

public class BackgroundMaskProcessor extends Processor {
    public BackgroundMaskProcessor() {
        super(new ProcessorBuilder()
            .setName("BackgroundMask")
            .setModule("Core")
            .setCategory("Image")
            .addInputProperty(new ProcessorProperty("Image", DataType.IMAGE))
            .addInputProperty(new ProcessorProperty("Threshold", DataType.NUMBER))
            .addInputProperty(new ProcessorProperty("History", DataType.NUMBER))
            .addOutputProperty(new ProcessorProperty("Output", DataType.IMAGE)));
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle state) {
        Mat image = inputs.read("Image");
        double threshold = inputs.read("Threshold");
        int history = inputs.<Double>read("History").intValue();

        BackgroundSubtractorMOG2 subtractorMOG2 = Video.createBackgroundSubtractorMOG2(history, threshold);
        Mat mask = new Mat();
        subtractorMOG2.apply(image, mask);

        DataBundle res = new DataBundle();
        res.write("Output", mask);
        return res;
    }
}
