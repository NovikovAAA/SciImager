package com.visualipcv.procs;

import com.visualipcv.core.*;

import java.util.ArrayList;
import java.util.List;

public class FileLoaderProcessor extends Processor {
    public FileLoaderProcessor() {
        super(new ProcessorBuilder()
            .setName("FileLoader")
            .setModule("Core")
            .setCategory("Input")
            .addInputProperty(new ProcessorProperty("Path", DataType.STRING))
            .addOutputProperty(new ProcessorProperty("File", DataType.BYTES)));
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle state) {
        return new DataBundle();
    }
}
