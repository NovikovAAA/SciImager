package com.visualipcv.procs;

import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.DataTypes;
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
            .addInputProperty(new ProcessorProperty("Data", DataTypes.ANY))
            .addOutputProperty(new ProcessorProperty("String", DataTypes.STRING)));
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle state) {
        Object data = inputs.read("Data");
        DataBundle outputs = new DataBundle();
        outputs.write("String", data.toString());
        return outputs;
    }
}
