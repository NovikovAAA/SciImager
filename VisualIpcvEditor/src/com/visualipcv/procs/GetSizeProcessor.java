package com.visualipcv.procs;

import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorProperty;
import org.opencv.core.Mat;

import java.util.ArrayList;

public class GetSizeProcessor extends Processor {
    public GetSizeProcessor() {
        super("GetSize", "Core", "Image",
                new ArrayList<ProcessorProperty>() {
                    {
                        add(new ProcessorProperty("Image", DataType.IMAGE));
                    }
                },
                new ArrayList<ProcessorProperty>() {
                    {
                        add(new ProcessorProperty("Width", DataType.NUMBER));
                        add(new ProcessorProperty("Height", DataType.NUMBER));
                    }
                });
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
