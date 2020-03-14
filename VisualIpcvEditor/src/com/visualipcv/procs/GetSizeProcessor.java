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
            .addOutputProperty(new ProcessorProperty("Width", DataType.NUMBER))
            .addOutputProperty(new ProcessorProperty("Height", DataType.NUMBER)));
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle state) {
        Mat image = inputs.read("Image");
        DataBundle outputs = new DataBundle();
        outputs.write("Width", (double)image.width());
        outputs.write("Height", (double)image.height());
        return outputs;
    }
}
