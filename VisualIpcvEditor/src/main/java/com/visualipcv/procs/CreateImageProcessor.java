package com.visualipcv.procs;

import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.DataTypes;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorBuilder;
import com.visualipcv.core.ProcessorProperty;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class CreateImageProcessor extends Processor {
    public CreateImageProcessor() {
        super(new ProcessorBuilder()
            .setName("CreateImage")
            .setModule("Core")
            .setCategory("Image")
            .addInputProperty(new ProcessorProperty("Width", DataTypes.INTEGER))
            .addInputProperty(new ProcessorProperty("Height", DataTypes.INTEGER))
            .addOutputProperty(new ProcessorProperty("Image", DataTypes.IMAGE)));
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle state) {
        DataBundle outputs = new DataBundle();
        outputs.write("Image", new Mat(inputs.<Integer>read("Width"), inputs.<Integer>read("Height"), CvType.makeType(4, 3)));
        return outputs;
    }
}
