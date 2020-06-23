package com.visualipcv.procs;

import com.visualipcv.core.CommonException;
import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.DataTypes;
import com.visualipcv.core.GlobalConstants;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorBuilder;
import com.visualipcv.core.ProcessorProperty;
import com.visualipcv.core.ProcessorPropertyBuilder;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class ImageCollectionSourceProcessor extends Processor {
    public ImageCollectionSourceProcessor() {
        super(new ProcessorBuilder()
            .setName("ImageCollectionSource")
            .setCategory("Input")
            .setModule("Core")
            .addInputProperty(new ProcessorProperty(new ProcessorPropertyBuilder("Path", DataTypes.DIRECTORY).addConnector().addControl()))
            .addOutputProperty(new ProcessorProperty(new ProcessorPropertyBuilder("Result", DataTypes.IMAGE).makeArray().addConnector())));
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle state) {
        File dir = new File((String)inputs.read("Path"));

        File[] images = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                for(String format : GlobalConstants.IMAGE_FORMAT) {
                    if(name.endsWith(format))
                        return true;
                }

                return false;
            }
        });

        List<Mat> result = new ArrayList<>();

        for(File file : images) {
            Mat newImage = Imgcodecs.imread(file.getAbsolutePath());

            if(newImage.empty())
                throw new CommonException("Cannot load image: " + file.getName());

            result.add(newImage);
        }

        DataBundle outputs = new DataBundle();
        outputs.write("Result", result);
        return outputs;
    }
}
