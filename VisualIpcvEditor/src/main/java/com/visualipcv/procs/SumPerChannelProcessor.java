package com.visualipcv.procs;

import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.DataTypes;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorBuilder;
import com.visualipcv.core.ProcessorProperty;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import java.util.ArrayList;

public class SumPerChannelProcessor extends Processor {
    public SumPerChannelProcessor() {
        super(new ProcessorBuilder()
            .setName("SumPerChannel")
            .setModule("Core")
            .setCategory("Image")
            .addInputProperty(new ProcessorProperty("Image", DataTypes.IMAGE))
            .addOutputProperty(new ProcessorProperty("Result", DataTypes.VECTOR4)));
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle state) {
        Mat image = inputs.read("Image");
        Scalar res = Core.sumElems(image);

        Double[] result = new Double[4];

        for(int i = 0; i < res.val.length; i++) {
            result[i] = res.val[i];
        }

        DataBundle outputs = new DataBundle();
        outputs.write("Result", result);
        return outputs;
    }
}
