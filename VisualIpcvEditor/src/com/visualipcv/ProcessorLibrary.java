package com.visualipcv;

import com.visualipcv.procs.*;

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

        initDefaultProcessors();
    }

    public static List<Processor> getProcessors() {
        return processors;
    }

    private static void initDefaultProcessors() {
        processors.add(new ConsoleOutputProcessor());
        processors.add(new StringSourceProcessor());
        processors.add(new NumberSourceProcessor());
        processors.add(new SumProcessor());
        processors.add(new NumberToStringProcessor());
    }
}
