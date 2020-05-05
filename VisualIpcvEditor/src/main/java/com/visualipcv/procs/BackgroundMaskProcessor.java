package com.visualipcv.procs;

import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.DataTypes;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorBuilder;
import com.visualipcv.core.ProcessorProperty;
import org.opencv.core.Mat;
import org.opencv.video.BackgroundSubtractorMOG2;
import org.opencv.video.Video;

import java.util.ArrayList;
import java.util.HashMap;

public class BackgroundMaskProcessor extends Processor {
    public BackgroundMaskProcessor() {
        super(new ProcessorBuilder()
            .setName("BackgroundMask")
            .setModule("Core")
            .setCategory("Image")
            .addInputProperty(new ProcessorProperty("Image", DataTypes.IMAGE))
            .addInputProperty(new ProcessorProperty("Threshold", DataTypes.DOUBLE))
            .addInputProperty(new ProcessorProperty("History", DataTypes.DOUBLE))
            .addOutputProperty(new ProcessorProperty("Output", DataTypes.IMAGE)));
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle props) {
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
