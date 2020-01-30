package com.visualipcv.procs;

import com.visualipcv.DataType;
import com.visualipcv.DataTypeLibrary;
import com.visualipcv.Processor;
import com.visualipcv.ProcessorProperty;

import java.util.ArrayList;
import java.util.List;

public class StringSourceProcessor extends Processor {
    public StringSourceProcessor() {
        super("StringSource", "Core", "Input",
                new ArrayList<ProcessorProperty>() {
                    {
                        add(new ProcessorProperty("String", DataTypeLibrary.getByName(DataType.STRING)));
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
        return new ArrayList<>(inputs);
    }
}
