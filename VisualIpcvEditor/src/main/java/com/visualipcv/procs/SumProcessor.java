package com.visualipcv.procs;

import com.visualipcv.core.*;

import java.util.ArrayList;
import java.util.List;

public class SumProcessor extends Processor {
    public SumProcessor() {
        super(new ProcessorBuilder()
            .setName("Sum")
            .setModule("Core")
            .setCategory("Math")
            .addInputProperty(new ProcessorProperty("A", DataType.DOUBLE))
            .addInputProperty(new ProcessorProperty("B", DataType.DOUBLE))
            .addOutputProperty(new ProcessorProperty("Result", DataType.DOUBLE)));
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle state) {
        DataBundle res = new DataBundle();
        res.write("Result", inputs.<Double>read("A") + inputs.<Double>read("B"));
        return res;
    }
}
