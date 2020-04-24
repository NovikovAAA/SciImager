package com.visualipcv.procs;

import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorBuilder;
import com.visualipcv.core.ProcessorProperty;
import org.opencv.core.Mat;

import java.util.ArrayList;

public class GetSizeProcessor extends Processor {
    public GetSizeProcessor() {
        super(new ProcessorBuilder()
            .setName("GetSize")
            .setModule("Core")
            .setCategory("Image")
            .addInputProperty(new ProcessorProperty("Image", DataType.IMAGE))
            .addOutputProperty(new ProcessorProperty("Width", DataType.INTEGER))
            .addOutputProperty(new ProcessorProperty("Height", DataType.INTEGER)));
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle state) {
        Mat image = inputs.read("Image");
        DataBundle outputs = new DataBundle();
        outputs.write("Width", image.width());
        outputs.write("Height", image.height());
        return outputs;
    }
}
