package com.visualipcv.view.dragdrop;

import com.visualipcv.core.Processor;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class ProcessorTransferable implements Transferable {
    private static final DataFlavor[] flavors = {
        ProcessorDataFlavor.PROCESSOR_DATA_FLAVOR
    };

    private Processor processor;

    public ProcessorTransferable(Processor processor) {
        this.processor = processor;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        for(DataFlavor f : flavors) {
            if(f.equals(flavor)) {
                return true;
            }
        }
        return false;
    }

    public Processor getProcessor() {
        return processor;
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if(isDataFlavorSupported(flavor)) {
            return getProcessor();
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }
}
