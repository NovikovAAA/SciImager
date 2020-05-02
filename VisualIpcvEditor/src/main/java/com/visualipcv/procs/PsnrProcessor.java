package com.visualipcv.procs;

import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.DataTypes;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorBuilder;
import com.visualipcv.core.ProcessorProperty;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class PsnrProcessor extends Processor {
    public PsnrProcessor() {
        super(new ProcessorBuilder()
            .setName("PSNR")
            .setModule("Core")
            .setCategory("Image")
            .addInputProperty(new ProcessorProperty("ImageA", DataTypes.IMAGE))
            .addInputProperty(new ProcessorProperty("ImageB", DataTypes.IMAGE))
            .addOutputProperty(new ProcessorProperty("Result", DataTypes.DOUBLE)));
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle state) {
        Mat imageA = inputs.read("ImageA");
        Mat imageB = inputs.read("ImageB");
        DataBundle outputs = new DataBundle();
        outputs.write("Result", calcPSNR(imageA, imageB));
        return outputs;
    }

    private double calcPSNR(Mat imageA, Mat imageB) {
        Mat res = new Mat();

        Core.absdiff(imageA, imageB, res);
        res.convertTo(res, CvType.CV_32F);
        res = res.mul(res);

        Scalar scalar = Core.sumElems(res);
        double sse = scalar.val[0] + scalar.val[1] + scalar.val[2];

        if(sse <= 1e-10)
            return 0.0;
        else {
            double mse = sse / (double)(imageA.channels() * imageA.total());
            return 10.0 * Math.log10((255 * 255) / mse);
        }
    }
}
