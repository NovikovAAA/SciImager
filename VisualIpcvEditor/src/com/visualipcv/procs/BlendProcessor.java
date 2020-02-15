package com.visualipcv.procs;

import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorProperty;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.util.ArrayList;

public class BlendProcessor extends Processor {
    public BlendProcessor() {
        super("Blend", "Core", "Image",
                new ArrayList<ProcessorProperty>() {
                    {
                        add(new ProcessorProperty("A", DataType.IMAGE));
                        add(new ProcessorProperty("B", DataType.IMAGE));
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
        Mat a = inputs.read("A");
        Mat b = inputs.read("B");
        Mat dst = new Mat();
        Core.add(a, b, dst);

        DataBundle res = new DataBundle();
        res.write("Result", dst);
        return res;
    }
}
