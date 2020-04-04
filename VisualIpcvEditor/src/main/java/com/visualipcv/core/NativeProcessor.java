package com.visualipcv.core;

import java.util.List;

public class NativeProcessor extends Processor {
    private static native List<ProcessorProperty> getInputPropertyList(ProcessorUID uid);
    private static native List<ProcessorProperty> getOutputPropertyList(ProcessorUID uid);
    private static native String getCategory(ProcessorUID uid);
    private static native DataBundle execute(ProcessorUID uid, DataBundle inputs);

    public NativeProcessor(ProcessorUID uid) {
        super(new ProcessorBuilder()
        .setName(uid.getName())
        .setModule(uid.getModule())
        .setCategory(getCategory(uid)));
        getInputProperties().addAll(getInputPropertyList(uid));
        getOutputProperties().addAll(getOutputPropertyList(uid));
    }

    public DataBundle execute(DataBundle inputs, DataBundle state) {
        return execute(new ProcessorUID(getName(), getModule()), inputs);
    }
}
