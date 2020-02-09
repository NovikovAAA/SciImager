package com.visualipcv.procs;

import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorProperty;

import java.util.ArrayList;

public class Vector4SourceProcessor extends Processor {
    public Vector4SourceProcessor() {
        super("Vector4Source", "Core", "Vectors",
                new ArrayList<ProcessorProperty>() {
                    {
                        add(new ProcessorProperty("Vector3", DataType.VECTOR4, true, false));
                    }
                },
                new ArrayList<ProcessorProperty>() {
                    {
                        add(new ProcessorProperty("Result", DataType.VECTOR4, true, true));
                    }
                });
    }

    @Override
    public DataBundle execute(DataBundle inputs) {
        Double[] values = (Double[])inputs.read("Vector3");
        DataBundle res = new DataBundle();
        res.write("Result", values);
        return res;
    }
}
