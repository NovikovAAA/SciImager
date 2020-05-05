package com.visualipcv.procs;

import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.DataTypes;
import com.visualipcv.core.GraphExecutionData;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorBuilder;
import com.visualipcv.core.ProcessorProperty;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.util.ArrayList;

public class CameraSource extends Processor {
    public CameraSource() {
        super(new ProcessorBuilder()
            .setName("CameraSource")
            .setModule("Core")
            .setCategory("Input")
            .addInputProperty(new ProcessorProperty("Index", DataTypes.INTEGER))
            .addOutputProperty(new ProcessorProperty("Output", DataTypes.IMAGE)));
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle props) {
        int index = inputs.read("Index");
        VideoCapture capture = CameraCaptureHelper.get(index);

        if(!capture.isOpened())
            return new DataBundle();

        DataBundle res = new DataBundle();
        Mat image = new Mat();
        capture.read(image);

        res.write("Output", image);
        return res;
    }
}
