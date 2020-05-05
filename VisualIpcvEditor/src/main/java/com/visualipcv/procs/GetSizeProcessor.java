package com.visualipcv.procs;

import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.DataTypes;
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
            .addInputProperty(new ProcessorProperty("Image", DataTypes.IMAGE))
            .addOutputProperty(new ProcessorProperty("Width", DataTypes.INTEGER))
            .addOutputProperty(new ProcessorProperty("Height", DataTypes.INTEGER)));
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle props) {
        Mat image = inputs.read("Image");
        DataBundle outputs = new DataBundle();
        outputs.write("Width", image.width());
        outputs.write("Height", image.height());
        return outputs;
    }
}
