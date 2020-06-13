package com.visualipcv.core;

import java.util.HashMap;
import java.util.List;

public class NativeProcessor extends Processor {
    private static native List<ProcessorProperty> getInputPropertyList(ProcessorUID uid);
    private static native List<ProcessorProperty> getOutputPropertyList(ProcessorUID uid);
    private static native String getCategory(ProcessorUID uid);
    private static native DataBundle execute(ProcessorUID uid, DataBundle inputs);
    private static native boolean getIsProperty(ProcessorUID uid);

    public NativeProcessor(ProcessorUID uid) {
        super(new ProcessorBuilder()
        .setName(uid.getName())
        .setModule(uid.getModule())
        .setCategory(getCategory(uid))
        .setInputProperties(getInputPropertyList(uid))
        .setOutputProperties(getOutputPropertyList(uid))
        .setIsProperty(getIsProperty(uid)));
    }

    public DataBundle execute(DataBundle inputs, DataBundle props) {
        return execute(new ProcessorUID(getName(), getModule()), inputs);
    }
}
