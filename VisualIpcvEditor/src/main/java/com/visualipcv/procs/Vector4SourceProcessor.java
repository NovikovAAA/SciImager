package com.visualipcv.procs;

import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.DataTypes;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorBuilder;
import com.visualipcv.core.ProcessorProperty;

import java.util.ArrayList;

public class Vector4SourceProcessor extends Processor {
    public Vector4SourceProcessor() {
        super(new ProcessorBuilder()
            .setName("Vector4Source")
            .setModule("Core")
            .setCategory("Math")
            .addInputProperty(new ProcessorProperty("Vector4", DataTypes.VECTOR4, true, false))
            .addOutputProperty(new ProcessorProperty("Result", DataTypes.VECTOR4, true, true)));
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle state) {
        Double[] values = (Double[])inputs.read("Vector3");
        DataBundle res = new DataBundle();
        res.write("Result", values);
        return res;
    }
}
