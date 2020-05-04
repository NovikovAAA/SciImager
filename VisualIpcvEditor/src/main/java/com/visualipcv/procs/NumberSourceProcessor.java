package com.visualipcv.procs;

import com.visualipcv.core.*;

import java.util.ArrayList;
import java.util.List;

public class NumberSourceProcessor extends Processor {
    public NumberSourceProcessor() {
        super(new ProcessorBuilder()
            .setName("NumberSource")
            .setModule("Core")
            .setCategory("Input")
            .addInputProperty(new ProcessorProperty("Number", DataTypes.DOUBLE, true, false))
            .addOutputProperty(new ProcessorProperty("Result", DataTypes.DOUBLE)));
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle state) {
        DataBundle res = new DataBundle();
        res.write("Result", inputs.read("Number"));
        return res;
    }
}
