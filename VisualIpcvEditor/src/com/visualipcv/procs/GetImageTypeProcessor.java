package com.visualipcv.procs;

import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.OpenCvDataTypes;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorProperty;
import org.opencv.core.Mat;

import java.util.ArrayList;

public class GetImageTypeProcessor extends Processor {
    public GetImageTypeProcessor() {
        super("GetImageType", "Core", "Image",
                new ArrayList<ProcessorProperty>() {
                    {
                        add(new ProcessorProperty("Image", DataType.IMAGE));
                    }
                },
                new ArrayList<ProcessorProperty>() {
                    {
                        add(new ProcessorProperty("Type", OpenCvDataTypes.CV_IMAGE_TYPE));
                    }
                });
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle state) {
        Mat image = inputs.read("Image");
        DataBundle outputs = new DataBundle();
        outputs.write("Type", image.type());
        return outputs;
    }
}
