package com.visualipcv.procs;

import com.visualipcv.*;
import com.visualipcv.core.*;

import java.util.ArrayList;
import java.util.List;

public class ConsoleOutputProcessor extends Processor {
    public ConsoleOutputProcessor() {
        super(new ProcessorBuilder()
            .setName("ConsoleOutput")
            .setModule("Core")
            .setCategory("Output")
            .addInputProperty(new ProcessorProperty("Text", DataType.STRING, true, true)));
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle state) {
        DataBundle bundle = new DataBundle();
        Console.output(inputs.read("Text"));
        return new DataBundle();
    }
}
