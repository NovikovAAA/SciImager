package com.visualipcv;

import java.util.List;

public class ProcessorLibrary {
    private Processor[] processorList;

    private native Object[] getProcessorList();

    public ProcessorLibrary() {
        this.processorList = (Processor[])getProcessorList();
    }

    public Processor[] getProcessors() {
        return processorList;
    }
}
