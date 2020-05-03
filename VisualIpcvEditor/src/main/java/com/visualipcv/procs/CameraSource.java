package com.visualipcv.procs;

import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.DataTypes;
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
    public DataBundle execute(DataBundle inputs, DataBundle state) {
        VideoCapture capture = state.read("Capture");
        int lastIndex = state.read("Index");
        int newIndex = inputs.<Double>read("Index").intValue();

        if(lastIndex != newIndex) {
            state.write("Index", newIndex);
            capture.open(newIndex);
        }

        if(!capture.isOpened())
            return new DataBundle();

        DataBundle res = new DataBundle();
        Mat image = new Mat();
        capture.read(image);

        res.write("Output", image);
        return res;
    }

    @Override
    public void onCreate(DataBundle state) {
        state.write("Capture", new VideoCapture());
        state.write("Index", -1);
    }

    @Override
    public void onDestroy(DataBundle state) {
        state.<VideoCapture>read("Capture").release();
    }
}
