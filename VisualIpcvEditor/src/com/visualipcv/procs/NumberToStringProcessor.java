package com.visualipcv.procs;

import com.visualipcv.core.*;

import java.util.ArrayList;

public class NumberToStringProcessor extends Processor {
    public NumberToStringProcessor() {
        super("Number->String", "Core", "Converters",
                new ArrayList<ProcessorProperty>() {
                    {
                        add(new ProcessorProperty("Number", DataType.NUMBER, false, true));
                    }
                },
                new ArrayList<ProcessorProperty>() {
                    {
                        add(new ProcessorProperty("String", DataType.STRING));
                    }
                });
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle state) {
        DataBundle res = new DataBundle();
        res.write("String", inputs.read("Number").toString());
        return res;
    }
}
