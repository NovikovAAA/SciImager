package com.visualipcv.procs;

import com.visualipcv.DataType;
import com.visualipcv.DataTypeLibrary;
import com.visualipcv.Processor;
import com.visualipcv.ProcessorProperty;

import java.util.ArrayList;
import java.util.List;

public class SumProcessor extends Processor {
    public SumProcessor() {
        super("Sum", "Core", "Math",
                new ArrayList<ProcessorProperty>() {
                    {
                        add(new ProcessorProperty("A", DataTypeLibrary.getByName(DataType.NUMBER)));
                        add(new ProcessorProperty("B", DataTypeLibrary.getByName(DataType.NUMBER)));
                    }
                },
                new ArrayList<ProcessorProperty>() {
                    {
                        add(new ProcessorProperty("Result", DataTypeLibrary.getByName(DataType.NUMBER)));
                    }
                });
    }

    @Override
    public List<Object> execute(List<Object> inputs) {
        return new ArrayList<Object>() {
            {
                add((double)inputs.get(0) + (double)inputs.get(1));
            }
        };
    }
}
