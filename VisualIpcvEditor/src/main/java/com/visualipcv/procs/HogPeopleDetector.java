package com.visualipcv.procs;

import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.DataTypes;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorBuilder;
import com.visualipcv.core.ProcessorProperty;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.HOGDescriptor;

public class HogPeopleDetector extends Processor {
    public HogPeopleDetector() {
        super(new ProcessorBuilder()
            .setName("HogPeopleDetector")
            .setModule("Core")
            .setCategory("Analyze")
            .addInputProperty(new ProcessorProperty("Image", DataTypes.IMAGE))
            .addOutputProperty(new ProcessorProperty("Result", DataTypes.IMAGE)));
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle state) {
        Mat image = inputs.read("Image");
        HOGDescriptor hog = new HOGDescriptor();
        hog.setSVMDetector(HOGDescriptor.getDaimlerPeopleDetector());
        MatOfRect matRects = new MatOfRect();
        MatOfDouble weights = new MatOfDouble();
        hog.detectMultiScale(image, matRects, weights, 125, new Size(8, 8));

        Rect[] rects = matRects.toArray();
        Mat out = new Mat();
        image.copyTo(out);

        for(Rect rect : rects) {
            Imgproc.rectangle(out, rect, new Scalar(0.0, 1.0, 0.0, 1.0));
        }

        DataBundle result = new DataBundle();
        result.write("Result", out);
        return result;
    }
}
