package com.visualipcv.procs;

import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.DataTypes;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorBuilder;
import com.visualipcv.core.ProcessorProperty;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.util.ArrayList;

public class BlendProcessor extends Processor {
    public BlendProcessor() {
        super(new ProcessorBuilder()
            .setName("Blend")
            .setModule("Core")
            .setCategory("Image")
            .addInputProperty(new ProcessorProperty("A", DataTypes.IMAGE))
            .addInputProperty(new ProcessorProperty("B", DataTypes.IMAGE))
            .addOutputProperty(new ProcessorProperty("Result", DataTypes.IMAGE)));
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle props) {
        Mat a = inputs.read("A");
        Mat b = inputs.read("B");
        Mat dst = new Mat();
        Core.add(a, b, dst);

        DataBundle res = new DataBundle();
        res.write("Result", dst);
        return res;
    }
}
