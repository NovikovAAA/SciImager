package com.visualipcv.procs;

import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.OpenCvDataTypes;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorProperty;

import java.util.ArrayList;

public class ConvertImageProcessor extends Processor {
    public ConvertImageProcessor() {
        super("ConvertImage", "Core", "Image",
                new ArrayList<ProcessorProperty>() {
                    {
                        add(new ProcessorProperty("Image", DataType.IMAGE));
                    }
                },
                new ArrayList<ProcessorProperty>() {
                    {
                        add(new ProcessorProperty("Target", OpenCvDataTypes.CV_IMAGE_TYPE));
                    }
                });
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle state) {
        
    }
}
