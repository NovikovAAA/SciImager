package com.visualipcv.procs;

import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorProperty;

import java.util.ArrayList;

public class Vector3BreakProcessor extends Processor {
    public Vector3BreakProcessor() {
        super("Vector3Break", "Core", "Vectors",
                new ArrayList<ProcessorProperty>() {
                    {
                        add(new ProcessorProperty("Vector3", DataType.VECTOR3));
                    }
                },
                new ArrayList<ProcessorProperty>() {
                    {
                        add(new ProcessorProperty("X", DataType.NUMBER));
                        add(new ProcessorProperty("Y", DataType.NUMBER));
                        add(new ProcessorProperty("Z", DataType.NUMBER));
                    }
                });
    }

    @Override
    public DataBundle execute(DataBundle inputs) {
        Double[] in = inputs.read("Vector3");
        DataBundle res = new DataBundle();
        res.write("X", in[0]);
        res.write("Y", in[1]);
        res.write("Z", in[2]);
        return res;
    }
}
