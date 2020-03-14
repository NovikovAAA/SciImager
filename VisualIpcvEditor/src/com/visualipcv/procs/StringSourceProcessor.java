package com.visualipcv.procs;

import com.visualipcv.core.*;

import java.util.ArrayList;
import java.util.List;

public class StringSourceProcessor extends Processor {
    public StringSourceProcessor() {
        super(new ProcessorBuilder()
            .setName("StringSource")
            .setModule("Core")
            .setCategory("Input")
            .addInputProperty(new ProcessorProperty("String", DataType.STRING, true, false))
            .addOutputProperty(new ProcessorProperty("String", DataType.STRING)));
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle state) {
        DataBundle res = new DataBundle();
        res.write("String", inputs.read("String"));
        return res;
    }
}
