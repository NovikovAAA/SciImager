package com.visualipcv.procs;

import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorProperty;

import java.util.ArrayList;

public class Vector4BreakProcessor extends Processor {
    public Vector4BreakProcessor() {
        super("Vector4Break", "Core", "Vectors",
                new ArrayList<ProcessorProperty>() {
                    {
                        add(new ProcessorProperty("Vector4", DataType.VECTOR4));
                    }
                },
                new ArrayList<ProcessorProperty>() {
                    {
                        add(new ProcessorProperty("X", DataType.NUMBER));
                        add(new ProcessorProperty("Y", DataType.NUMBER));
                        add(new ProcessorProperty("Z", DataType.NUMBER));
                        add(new ProcessorProperty("W", DataType.NUMBER));
                    }
                });
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
