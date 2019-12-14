package com.visualipcv.view;

import com.visualipcv.Processor;

import java.awt.datatransfer.DataFlavor;

public class ProcessorDataFlavor extends DataFlavor {
    public static final ProcessorDataFlavor PROCESSOR_DATA_FLAVOR = new ProcessorDataFlavor();

    public ProcessorDataFlavor() {
        super(Processor.class, null);
    }
}
