package com.visualipcv.procs;

import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorBuilder;
import com.visualipcv.core.ProcessorProperty;

import java.util.ArrayList;

public class Vector4BreakProcessor extends Processor {
    public Vector4BreakProcessor() {
        super(new ProcessorBuilder()
            .setName("Vector4Break")
            .setModule("Core")
            .setCategory("Math")
            .addInputProperty(new ProcessorProperty("Vector4", DataType.VECTOR4))
            .addOutputProperty(new ProcessorProperty("X", DataType.DOUBLE))
            .addOutputProperty(new ProcessorProperty("Y", DataType.DOUBLE))
            .addOutputProperty(new ProcessorProperty("Z", DataType.DOUBLE))
            .addOutputProperty(new ProcessorProperty("W", DataType.DOUBLE)));
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle state) {
        Double[] in = inputs.read("Vector4");
        DataBundle res = new DataBundle();
        res.write("X", in[0]);
        res.write("Y", in[1]);
        res.write("Z", in[2]);
        res.write("W", in[3]);
        return res;
    }
}
