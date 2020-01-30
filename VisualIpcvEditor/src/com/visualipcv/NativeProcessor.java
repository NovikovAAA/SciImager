package com.visualipcv;

import java.util.ArrayList;
import java.util.List;

public class NativeProcessor extends Processor {
    private static native List<ProcessorProperty> getInputPropertyList(ProcessorUID uid);
    private static native List<ProcessorProperty> getOutputPropertyList(ProcessorUID uid);
    private static native String getCategory(ProcessorUID uid);
    private static native List<Object> execute(ProcessorUID uid, List<Object> inputs);

    public NativeProcessor(ProcessorUID uid) {
        super(uid.getName(), uid.getModule(), getCategory(uid), getInputPropertyList(uid), getOutputPropertyList(uid));
    }

    public List<Object> execute(List<Object> inputs) {
        return execute(new ProcessorUID(getName(), getModule()), inputs);
    }
}
