package com.visualipcv.view.dragdrop;

import com.visualipcv.view.NodeSlotView;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class SlotTransferable implements Transferable {
    private SlotDataFlavor[] flavors = {
        SlotDataFlavor.SLOT_DATA_FLAVOR
    };

    private NodeSlotView slot;

    public SlotTransferable(NodeSlotView slot) {
        this.slot = slot;
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

    public NodeSlotView getSlot() {
        return slot;
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if(isDataFlavorSupported(flavor)) {
            return getSlot();
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }
}
