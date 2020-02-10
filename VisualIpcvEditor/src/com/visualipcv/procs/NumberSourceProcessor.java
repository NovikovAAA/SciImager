package com.visualipcv.procs;

import com.visualipcv.core.*;

import java.util.ArrayList;
import java.util.List;

public class NumberSourceProcessor extends Processor {
    public NumberSourceProcessor() {
        super("NumberSource", "Core", "Input",
                new ArrayList<ProcessorProperty>() {
                    {
                        add(new ProcessorProperty("Number", DataType.NUMBER, true, false));
                    }
                },
                new ArrayList<ProcessorProperty>() {
                    {
                        add(new ProcessorProperty("Number", DataType.NUMBER));
                    }
                });
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle state) {
        DataBundle res = new DataBundle();
        res.write("Number", inputs.read("Number"));
        return res;
    }
}
