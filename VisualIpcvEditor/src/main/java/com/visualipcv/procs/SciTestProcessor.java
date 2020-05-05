package com.visualipcv.procs;

import com.visualipcv.core.DataType;
import com.visualipcv.core.DataTypes;
import com.visualipcv.core.DocumentManager;
import com.visualipcv.core.ProcessorProperty;
import com.visualipcv.core.SciProcessor;
import com.visualipcv.scripts.SciScript;

public class SciTestProcessor extends SciProcessor {
    public SciTestProcessor() {
        super("Test", "Test", "Test", new SciScript(DocumentManager.getActiveDocument()));
        getScript().setCode("Result=Image*K");
        getScript().addInputProperty(new ProcessorProperty("Image", DataTypes.IMAGE));
        getScript().addInputProperty(new ProcessorProperty("K", DataTypes.DOUBLE));
        getScript().addOutputProperty(new ProcessorProperty("Result", DataTypes.IMAGE));
    }
}
