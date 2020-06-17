package com.visualipcv.procs;

import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.DataTypes;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorBuilder;
import com.visualipcv.core.ProcessorProperty;
import com.visualipcv.core.ProcessorPropertyBuilder;

import java.util.List;

public class IteratorProcessor extends Processor {
    public IteratorProcessor() {
        super(new ProcessorBuilder()
            .setName("Iterator")
            .setCategory("Control")
            .setModule("Core")
            .addInputProperty(new ProcessorProperty(new ProcessorPropertyBuilder("Data", DataTypes.ANY).addConnector().makeArray()))
            .addOutputProperty(new ProcessorProperty(new ProcessorPropertyBuilder("Element", DataTypes.ANY).addConnector()))
            .addOutputProperty(new ProcessorProperty(new ProcessorPropertyBuilder("Index", DataTypes.INTEGER).addConnector())));
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle state) {
        DataBundle outputs = new DataBundle();
        Integer index = state.read("Index");

        if(index == null)
            index = 0;

        index = index % ((List<Object>)inputs.read("Data")).size();
        outputs.write("Element", ((List<Object>)inputs.read("Data")).get(index));
        outputs.write("Index", index);
        state.write("Index", index + 1);

        return outputs;
    }
}
