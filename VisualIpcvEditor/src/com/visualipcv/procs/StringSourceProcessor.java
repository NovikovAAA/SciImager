package com.visualipcv.procs;

import com.visualipcv.core.*;

import java.util.ArrayList;
import java.util.List;

public class StringSourceProcessor extends Processor {
    public StringSourceProcessor() {
        super("StringSource", "Core", "Input",
                new ArrayList<ProcessorProperty>() {
                    {
                        add(new ProcessorProperty("String", DataType.STRING, true, false));
                    }
                },
                new ArrayList<ProcessorProperty>() {
                    {
                        add(new ProcessorProperty("String", DataType.STRING));
                    }
                });
    }

    @Override
    public DataBundle execute(DataBundle inputs) {
        DataBundle res = new DataBundle();
        res.write("String", inputs.read("String"));
        return res;
    }
}
