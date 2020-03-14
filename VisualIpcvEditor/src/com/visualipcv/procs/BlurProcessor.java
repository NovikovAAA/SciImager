package com.visualipcv.procs;

import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorBuilder;
import com.visualipcv.core.ProcessorProperty;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

public class BlurProcessor extends Processor {
    public BlurProcessor() {
        super(new ProcessorBuilder()
            .setName("Blur")
            .setModule("Core")
            .setCategory("Image")
            .addInputProperty(new ProcessorProperty("Image", DataType.IMAGE))
            .addInputProperty(new ProcessorProperty("SizeX", DataType.NUMBER))
            .addInputProperty(new ProcessorProperty("SizeY", DataType.NUMBER))
            .addOutputProperty(new ProcessorProperty("Output", DataType.NUMBER)));
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle state) {
        Mat image = inputs.read("Image");

        if(image == null) {
            return new DataBundle();
        }

        Double sizeX = inputs.read("SizeX");
        Double sizeY = inputs.read("SizeY");

        sizeX = Double.max(sizeX, 1);
        sizeY = Double.max(sizeY, 1);

        Mat dst = new Mat(image.width(), image.height(), image.type());
        Imgproc.blur(image, dst, new Size(sizeX, sizeY));

        DataBundle res = new DataBundle();
        res.write("Output", dst);
        return res;
    }
}
