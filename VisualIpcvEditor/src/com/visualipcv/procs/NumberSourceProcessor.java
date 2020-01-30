package com.visualipcv.procs;

import com.visualipcv.DataType;
import com.visualipcv.DataTypeLibrary;
import com.visualipcv.Processor;
import com.visualipcv.ProcessorProperty;

import java.util.ArrayList;
import java.util.List;

public class NumberSourceProcessor extends Processor {
    public NumberSourceProcessor() {
        super("NumberSource", "Core", "Input",
                new ArrayList<ProcessorProperty>() {
                    {
                        add(new ProcessorProperty("Number", DataTypeLibrary.getByName(DataType.NUMBER)));
                    }
                },
                new ArrayList<ProcessorProperty>() {
                    {
                        add(new ProcessorProperty("Number", DataTypeLibrary.getByName(DataType.NUMBER)));
                    }
                });
    }

    @Override
    public List<Object> execute(List<Object> inputs) {
        return new ArrayList<>(inputs);
    }
}
