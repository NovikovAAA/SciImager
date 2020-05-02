package com.visualipcv.procs;

import com.visualipcv.core.CommonException;
import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.DataTypes;
import com.visualipcv.core.GraphExecutionException;
import com.visualipcv.core.Node;
import com.visualipcv.core.NodeSlot;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorBuilder;
import com.visualipcv.core.ProcessorProperty;
import com.visualipcv.utils.ProcUtils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

public class AbsDiffProcessor extends Processor {
    public AbsDiffProcessor() {
        super(new ProcessorBuilder()
                .setName("AbsDiff")
                .setModule("Core")
                .setCategory("Math")
                .addInputProperty(new ProcessorProperty("A", DataTypes.ANY))
                .addInputProperty(new ProcessorProperty("B", DataTypes.ANY))
                .addOutputProperty(new ProcessorProperty("Result", DataTypes.ANY)));
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle state) throws CommonException {
        Object a = inputs.read("A");
        Object b = inputs.read("B");

        if(a.getClass() != b.getClass()) {
            throw new CommonException("Operand types mismatch");
        }

        Object result;

        if(a instanceof Double) {
            result = Math.abs((double)a - (double)b);
        } else if(a instanceof Double[]) {
            Double[] A = (Double[])a;
            Double[] B = (Double[])b;

            if(A.length != B.length)
                throw new CommonException("Operand types mismatch");

            result = new Double[A.length];

            for(int i = 0; i < A.length; i++) {
                ((Double[])result)[i] = Math.abs(A[i] - B[i]);
            }
        } else if(a instanceof Mat) {
            result = new Mat();
            Core.absdiff((Mat)a, (Mat)b, (Mat)result);
        } else {
            throw new CommonException("Unsupported operand type");
        }

        DataBundle outputs = new DataBundle();
        outputs.write("Result", result);
        return outputs;
    }
}
