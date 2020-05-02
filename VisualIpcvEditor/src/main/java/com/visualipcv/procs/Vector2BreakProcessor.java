package com.visualipcv.procs;

import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.DataTypes;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorBuilder;
import com.visualipcv.core.ProcessorProperty;

import java.util.ArrayList;

public class Vector2BreakProcessor extends Processor {
    public Vector2BreakProcessor() {
        super(new ProcessorBuilder()
            .setName("Vector2Break")
            .setModule("Core")
            .setCategory("Math")
            .addInputProperty(new ProcessorProperty("Vector2", DataTypes.VECTOR2))
            .addOutputProperty(new ProcessorProperty("X", DataTypes.DOUBLE))
            .addOutputProperty(new ProcessorProperty("Y", DataTypes.DOUBLE)));
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle state) {
        Double[] in = inputs.read("Vector2");
        DataBundle res = new DataBundle();
        res.write("X", in[0]);
        res.write("Y", in[1]);
        return res;
    }
}
