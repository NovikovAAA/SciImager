package com.visualipcv.procs;

import com.visualipcv.core.CommonException;
import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.DataTypes;
import com.visualipcv.core.GraphExecutionException;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorBuilder;
import com.visualipcv.core.ProcessorProperty;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ImageSourceProcessor extends Processor {
    public ImageSourceProcessor() {
        super(new ProcessorBuilder()
            .setName("ImageSource")
            .setModule("Core")
            .setCategory("Input")
            .addInputProperty(new ProcessorProperty("Path", DataTypes.FILE))
            .addOutputProperty(new ProcessorProperty("Image", DataTypes.IMAGE)));
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle state) throws CommonException {
        String path = inputs.read("Path");
        DataBundle res = new DataBundle();
        Mat image = Imgcodecs.imread(path);

        if(image.empty())
            throw new CommonException("Image not found");

        res.write("Image", image);
        return res;
    }
}
