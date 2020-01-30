package com.visualipcv.procs;

import com.visualipcv.DataType;
import com.visualipcv.DataTypeLibrary;
import com.visualipcv.Processor;
import com.visualipcv.ProcessorProperty;

import java.util.ArrayList;
import java.util.List;

public class NumberToStringProcessor extends Processor {
    public NumberToStringProcessor() {
        super("Number->String", "Core", "Converters",
                new ArrayList<ProcessorProperty>() {
                    {
                        add(new ProcessorProperty("Number", DataTypeLibrary.getByName(DataType.NUMBER)));
                    }
                },
                new ArrayList<ProcessorProperty>() {
                    {
                        add(new ProcessorProperty("String", DataTypeLibrary.getByName(DataType.STRING)));
                    }
                });
    }

    @Override
    public List<Object> execute(List<Object> inputs) {
        return new ArrayList<Object>() {
            {
                add(Double.toString((double)inputs.get(0)));
            }
        };
    }
}
