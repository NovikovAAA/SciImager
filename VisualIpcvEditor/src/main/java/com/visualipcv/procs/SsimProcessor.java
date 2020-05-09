package com.visualipcv.procs;

import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataTypes;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorBuilder;
import com.visualipcv.core.ProcessorProperty;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class SsimProcessor extends Processor {
    public SsimProcessor() {
        super(new ProcessorBuilder()
            .setName("SSIM")
            .setModule("Core")
            .setCategory("Image")
            .addInputProperty(new ProcessorProperty("ImageA", DataTypes.IMAGE))
            .addInputProperty(new ProcessorProperty("ImageB", DataTypes.IMAGE))
            .addOutputProperty(new ProcessorProperty("Result", DataTypes.VECTOR4)));
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle state) {
        Mat image1 = inputs.read("ImageA");
        Mat image2 = inputs.read("ImageB");
        DataBundle outputs = new DataBundle();

        double[] res = calcSsim(image1, image2);
        Double[] Res = new Double[4];

        for(int i = 0; i < Math.min(res.length, 4); i++) {
            Res[i] = res[i];
        }

        outputs.write("Result", Res);
        return outputs;
    }

    private double[] calcSsim(Mat i1, Mat i2) {
        final double C1 = 6.5025, C2 = 58.5225;
        int d = CvType.CV_32F;

        Mat I1 = new Mat();
        Mat I2 = new Mat();

        i1.convertTo(I1, d);
        i2.convertTo(I2, d);

        Mat I2_2   = I2.mul(I2);
        Mat I1_2   = I1.mul(I1);
        Mat I1_I2  = I1.mul(I2);

        Mat mu1 = new Mat(), mu2 = new Mat();
        Imgproc.GaussianBlur(I1, mu1, new Size(11, 11), 1.5);
        Imgproc.GaussianBlur(I2, mu2, new Size(11, 11), 1.5);

        Mat mu1_2 = mu1.mul(mu1);
        Mat mu2_2 = mu2.mul(mu2);
        Mat mu1_mu2 = mu1.mul(mu2);

        Mat sigma1_2 = new Mat(), sigma2_2 = new Mat(), sigma12 = new Mat();

        Imgproc.GaussianBlur(I1_2, sigma1_2, new Size(11, 11), 1.5);
        Core.subtract(sigma1_2, mu1_2, sigma1_2);

        Imgproc.GaussianBlur(I2_2, sigma2_2, new Size(11, 11), 1.5);
        Core.subtract(sigma2_2, mu2_2, sigma2_2);

        Imgproc.GaussianBlur(I1_I2, sigma12, new Size(11, 11), 1.5);
        Core.subtract(sigma12, mu1_mu2, sigma12);

        Mat t1 = new Mat(), t2 = new Mat(), t3 = new Mat();

        Core.multiply(mu1_mu2, new Scalar(2.0), t1);
        Core.add(t1, new Scalar(C1), t1);

        Core.multiply(sigma12, new Scalar(2.0), t2);
        Core.add(t2, new Scalar(C2), t2);

        t3 = t1.mul(t2);

        Core.add(mu1_2, mu2_2, t1);
        Core.add(t1, new Scalar(C1), t1);

        Core.add(sigma1_2, sigma2_2, t2);
        Core.add(t2, new Scalar(C2), t2);

        t1 = t1.mul(t2);

        Mat ssim_map = new Mat();
        Core.divide(t3, t1, ssim_map);

        Scalar mssim = Core.mean(ssim_map);
        return mssim.val;
    }
}
