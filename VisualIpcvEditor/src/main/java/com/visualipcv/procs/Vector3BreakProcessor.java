package com.visualipcv.procs;

import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorBuilder;
import com.visualipcv.core.ProcessorProperty;

import java.util.ArrayList;

public class Vector3BreakProcessor extends Processor {
    public Vector3BreakProcessor() {
        super(new ProcessorBuilder()
            .setName("Vector3Break")
            .setModule("Core")
            .setCategory("Math")
            .addInputProperty(new ProcessorProperty("Vector3", DataType.VECTOR3))
            .addOutputProperty(new ProcessorProperty("X", DataType.DOUBLE))
            .addOutputProperty(new ProcessorProperty("Y", DataType.DOUBLE))
            .addOutputProperty(new ProcessorProperty("Z", DataType.DOUBLE)));
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle state) {
        Double[] in = inputs.read("Vector3");
        DataBundle res = new DataBundle();
        res.write("X", in[0]);
        res.write("Y", in[1]);
        res.write("Z", in[2]);
        return res;
    }
}
