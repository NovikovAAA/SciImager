package com.visualipcv.procs;

import com.visualipcv.*;
import com.visualipcv.core.*;

import java.util.ArrayList;
import java.util.List;

public class ConsoleOutputProcessor extends Processor {
    public ConsoleOutputProcessor() {
        super("ConsoleOutput", "Core", "Output",
                new ArrayList<ProcessorProperty>() {
                    {
                        add(new ProcessorProperty("Text", DataType.STRING, false, true));
                    }
                },
                new ArrayList<>());
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle state) {
        DataBundle bundle = new DataBundle();
        Console.output(inputs.read("Text"));
        return new DataBundle();
    }
}
