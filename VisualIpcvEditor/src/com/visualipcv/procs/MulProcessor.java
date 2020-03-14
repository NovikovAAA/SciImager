package com.visualipcv.procs;

import com.visualipcv.core.CommonException;
import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.Node;
import com.visualipcv.core.NodeSlot;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorProperty;
import com.visualipcv.utils.ProcUtils;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.util.ArrayList;

public class MulProcessor extends Processor {
    public MulProcessor() {
        super("Mul", "Core", "Math",
                new ArrayList<ProcessorProperty>() {
                    {
                        add(new ProcessorProperty("A", DataType.ANY));
                        add(new ProcessorProperty("B", DataType.ANY));
                    }
                },
                new ArrayList<ProcessorProperty>() {
                    {
                        add(new ProcessorProperty("Result", DataType.ANY));
                    }
                });
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle state) throws CommonException {
        Object a = inputs.read("A");
        Object b = inputs.read("B");

        if(a.getClass() != b.getClass())
            throw new CommonException("Operand types mismatch");

        Object result = null;

        if(a instanceof Double) {
            result = (double)a * (double)b;
        } else if(a instanceof Double[]) {
            Double[] A = (Double[])a;
            Double[] B = (Double[])b;

            if(A.length != B.length)
                throw new CommonException("Operand types mismatch");

            result = new Double[A.length];

            for(int i = 0; i < A.length; i++)
                ((Double[])result)[i] = A[i] * B[i];
        } else if(a instanceof Mat) {
            result = new Mat();
            Core.multiply((Mat)a, (Mat)b, (Mat)result);
        }

        DataBundle outputs = new DataBundle();
        outputs.write("Result", result);
        return outputs;
    }
}
