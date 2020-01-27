package com.visualipcv;

public class ProcessorLibrary {
    private static Processor[] processors;
    public static native Processor[] getProcessorList();

    static {
        processors = getProcessorList();
    }

    public static Processor[] getProcessors() {
        return processors;
    }
}
