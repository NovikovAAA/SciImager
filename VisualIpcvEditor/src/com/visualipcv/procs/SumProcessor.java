package com.visualipcv.procs;

import com.visualipcv.core.*;

import java.util.ArrayList;
import java.util.List;

public class SumProcessor extends Processor {
    public SumProcessor() {
        super("Sum", "Core", "Math",
                new ArrayList<ProcessorProperty>() {
                    {
                        add(new ProcessorProperty("A", DataType.NUMBER));
                        add(new ProcessorProperty("B", DataType.NUMBER));
                    }
                },
                new ArrayList<ProcessorProperty>() {
                    {
                        add(new ProcessorProperty("Result", DataType.NUMBER));
                    }
                });
    }

    @Override
    public DataBundle execute(DataBundle inputs) {
        DataBundle res = new DataBundle();
        res.write("Result", inputs.<Double>read("A") + inputs.<Double>read("B"));
        return res;
    }
}
