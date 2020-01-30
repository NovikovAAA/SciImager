package com.visualipcv;

import java.util.ArrayList;
import java.util.List;

public class ProcessorLibrary {
    private static List<Processor> processors = new ArrayList<>();
    public static native List<ProcessorUID> getProcessorList();

    static {
        List<ProcessorUID> processorList = getProcessorList();

        for(ProcessorUID uid : processorList) {
            processors.add(new NativeProcessor(uid));
        }
    }

    public static List<Processor> getProcessors() {
        return processors;
    }
}
