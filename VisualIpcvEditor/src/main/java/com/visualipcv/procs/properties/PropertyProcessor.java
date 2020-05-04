package com.visualipcv.procs.properties;

import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorBuilder;
import com.visualipcv.core.ProcessorProperty;

public class PropertyProcessor extends Processor {
    public PropertyProcessor(DataType dataType) {
        super(new ProcessorBuilder()
            .setName("Property" + dataType.getName())
            .setCategory("Property")
            .setModule("Core")
            .setIsProperty(true)
            .addInputProperty(new ProcessorProperty("Value", dataType, true, false))
            .addOutputProperty(new ProcessorProperty("Result", dataType)));
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle state) {
        DataBundle outputs = new DataBundle();
        outputs.write("Result", inputs.read("Value"));
        return outputs;
    }
}
