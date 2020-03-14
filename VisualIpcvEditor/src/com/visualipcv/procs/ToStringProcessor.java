package com.visualipcv.procs;

import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorBuilder;
import com.visualipcv.core.ProcessorProperty;

import java.awt.image.DataBufferUShort;
import java.util.ArrayList;

public class ToStringProcessor extends Processor {
    public ToStringProcessor() {
        super(new ProcessorBuilder()
            .setName("ToString")
            .setModule("Core")
            .setCategory("Image")
            .addInputProperty(new ProcessorProperty("Data", DataType.ANY))
            .addOutputProperty(new ProcessorProperty("String", DataType.STRING)));
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle state) {
        Object data = inputs.read("Data");
        DataBundle outputs = new DataBundle();
        outputs.write("String", data.toString());
        return outputs;
    }
}
