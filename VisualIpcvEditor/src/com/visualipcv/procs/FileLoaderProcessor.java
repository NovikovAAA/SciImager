package com.visualipcv.procs;

import com.visualipcv.core.*;

import java.util.ArrayList;
import java.util.List;

public class FileLoaderProcessor extends Processor {
    public FileLoaderProcessor() {
        super("FileLoader", "Core", "Input",
                new ArrayList<ProcessorProperty>() {
                    {
                        add(new ProcessorProperty("Path", DataType.STRING));
                    }
                },
                new ArrayList<ProcessorProperty>() {
                    {
                        add(new ProcessorProperty("File", DataType.BYTES));
                    }
                });
    }

    @Override
    public DataBundle execute(DataBundle inputs) {
        return new DataBundle();
    }
}
