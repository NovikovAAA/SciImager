package com.visualipcv.view.dragdrop;

import com.visualipcv.view.NodeSlotView;
import com.visualipcv.view.NodeView;

import java.awt.datatransfer.DataFlavor;

public class SlotDataFlavor extends DataFlavor {
    public static final SlotDataFlavor SLOT_DATA_FLAVOR = new SlotDataFlavor();

    public SlotDataFlavor() {
        super(NodeSlotView.class, null);
    }

    @Override
    public boolean isFlavorSerializedObjectType() {
        return false;
    }
}
