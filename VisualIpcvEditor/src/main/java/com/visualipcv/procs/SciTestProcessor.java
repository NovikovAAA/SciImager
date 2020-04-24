package com.visualipcv.procs;

import com.visualipcv.core.DataType;
import com.visualipcv.core.ProcessorProperty;
import com.visualipcv.core.SciProcessor;
import com.visualipcv.scripts.SciScript;

public class SciTestProcessor extends SciProcessor {
    public SciTestProcessor() {
        super("Test", "Test", "Test", new SciScript());
        getScript().setCode("Result=Image*K");
        getScript().addInputProperty(new ProcessorProperty("Image", DataType.IMAGE));
        getScript().addInputProperty(new ProcessorProperty("K", DataType.DOUBLE));
        getScript().addOutputProperty(new ProcessorProperty("Result", DataType.IMAGE));
    }
}
