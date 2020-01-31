package com.visualipcv.view.dragdrop;

import com.visualipcv.core.Processor;

import java.awt.datatransfer.DataFlavor;

public class ProcessorDataFlavor extends DataFlavor {
    public static final ProcessorDataFlavor PROCESSOR_DATA_FLAVOR = new ProcessorDataFlavor();

    public ProcessorDataFlavor() {
        super(Processor.class, null);
    }

    @Override
    public boolean isFlavorSerializedObjectType() {
        return false;
    }
}
