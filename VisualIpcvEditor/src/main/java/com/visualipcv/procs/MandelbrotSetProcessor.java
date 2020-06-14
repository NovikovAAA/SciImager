package com.visualipcv.procs;

import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.DataTypes;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorBuilder;
import com.visualipcv.core.ProcessorProperty;
import org.apache.commons.math3.complex.Complex;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.scilab.modules.gui.tree.SimpleTree;

public class MandelbrotSetProcessor extends Processor {
    public MandelbrotSetProcessor() {
        super(new ProcessorBuilder()
            .setName("MandelbrotSet")
            .setCategory("Image")
            .setModule("Core")
            .addInputProperty(new ProcessorProperty("Width", DataTypes.INTEGER))
            .addInputProperty(new ProcessorProperty("Height", DataTypes.INTEGER))
            .addInputProperty(new ProcessorProperty("Iterations", DataTypes.INTEGER))
            .addInputProperty(new ProcessorProperty("Scale", DataTypes.DOUBLE))
            .addInputProperty(new ProcessorProperty("Length", DataTypes.DOUBLE))
            .addOutputProperty(new ProcessorProperty("Result", DataTypes.IMAGE)));
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle state) {
        DataBundle outputs = new DataBundle();
        Mat image = new Mat(inputs.read("Height"), inputs.read("Width"), CvType.makeType(4, 3));
        calculate(image, inputs.read("Iterations"), inputs.read("Scale"), inputs.read("Length"));
        outputs.write("Result", image);
        return outputs;
    }

    private void calculate(Mat image, int iterations, double scale, double length) {
        for(int i = 0; i < image.width(); i++) {
            for(int j = 0; j < image.height(); j++) {
                int current = iterations;
                double x = (double)i;
                double y = (double)j;
                Complex z = new Complex(0, 0);
                Complex c = new Complex((x - image.width() * 0.5) / scale, (y - image.height() * 0.5) / scale);

                for(int k = 0; k < iterations; k++) {
                    z = z.multiply(z).add(c);
                    double len = Math.sqrt(z.getReal() * z.getReal() + z.getImaginary() * z.getImaginary());

                    if(len > length) {
                        current = k;
                        break;
                    }
                }

                double red = 0.1 + current / (double)iterations * 0.2;
                double green = 0.2 + current / (double)iterations * 0.3;
                double blue = 0.3 + current / (double)iterations * 0.1;

                image.put(j, i, new int[] { (int)(blue * 255.0), (int)(green * 255.0), (int)(red * 255.0) });
            }
        }
    }
}
