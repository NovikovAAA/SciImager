package com.visualipcv.procs;

import com.visualipcv.*;

import java.util.ArrayList;
import java.util.List;

public class ConsoleOutputProcessor extends Processor {
    public ConsoleOutputProcessor() {
        super("ConsoleOutput", "Core", "Output",
                new ArrayList<ProcessorProperty>() {
                    {
                        add(new ProcessorProperty("Text", DataTypeLibrary.getByName(DataType.STRING)));
                    }
                },
                new ArrayList<>());
    }

    @Override
    public List<Object> execute(List<Object> inputs) {
        Console.output((String)inputs.get(0));
        return new ArrayList<>();
    }
}
