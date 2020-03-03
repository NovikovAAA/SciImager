package com.visualipcv.procs;

import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorProperty;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

public class ResizeProcessor extends Processor {
    public ResizeProcessor() {
        super("Resize", "Core", "Image",
                new ArrayList<ProcessorProperty>() {
                    {
                        add(new ProcessorProperty("Image", DataType.IMAGE));
                        add(new ProcessorProperty("Width", DataType.NUMBER));
                        add(new ProcessorProperty("Height", DataType.NUMBER));
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
        Double width = inputs.read("Width");
        Double height = inputs.read("Height");

        DataBundle outputs = new DataBundle();
        Mat result = new Mat();
        outputs.write("Result", result);
        Imgproc.resize(image, result, new Size(width, height));

        return outputs;
    }
}
