package com.visualipcv.procs;

import com.sun.org.apache.xerces.internal.impl.dv.DatatypeValidator;
import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorProperty;

import java.util.ArrayList;

public class Vector2SourceProcessor extends Processor {
    public Vector2SourceProcessor() {
        super("Vector2Source", "Core", "Vectors",
                new ArrayList<ProcessorProperty>() {
                    {
                        add(new ProcessorProperty("Vector2", DataType.VECTOR2, true, false));
                    }
                },
                new ArrayList<ProcessorProperty>() {
                    {
                        add(new ProcessorProperty("Result", DataType.VECTOR2, false, true));
                    }
                });
    }

    @Override
    public DataBundle execute(DataBundle inputs) {
        Double[] value = inputs.read("Vector2");
        DataBundle res = new DataBundle();
        res.write("Result", value);
        return res;
    }
}