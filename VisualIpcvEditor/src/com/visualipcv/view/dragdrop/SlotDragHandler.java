package com.visualipcv.view.dragdrop;

import com.visualipcv.NodeSlot;
import com.visualipcv.Processor;
import com.visualipcv.view.NodeSlotType;
import com.visualipcv.view.NodeSlotView;
import com.visualipcv.view.SlotConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

public class SlotDragHandler extends TransferHandler {
    @Override
    public int getSourceActions(JComponent c) {
        return TransferHandler.LINK;
    }

    @Override
    public Transferable createTransferable(JComponent source) {
        NodeSlotView slot = (NodeSlotView)source;
        NodeSlotView other = null;

        if(slot.getType() == NodeSlotType.INPUT) {
            if(slot.getConnectionCount() >= 1) {
                other = slot.getConnection(0).getSourceSlot();
            } else {
                other = slot;
            }
        } else {
            other = slot;
        }

        slot.onBeginDrag(other);
        return new SlotTransferable(other);
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport support) {
        if(!support.isDrop()) {
            return false;
        }

        try {
            NodeSlotView slot = (NodeSlotView)support.getTransferable().getTransferData(SlotDataFlavor.SLOT_DATA_FLAVOR);
            NodeSlotView target = (NodeSlotView)support.getComponent();

            if(slot.getNode() == target.getNode()) {
                return false;
            }
            if(slot.getType() == target.getType()) {
                return false;
            }

            if(slot.getConnectionCount() >= 1 && slot.getType() == NodeSlotType.INPUT) {
                return false;
            }
            if(target.getConnectionCount() >= 1 && target.getType() == NodeSlotType.INPUT) {
                return false;
            }

        } catch(Exception e) {
            return false;
        }

        return support.isDataFlavorSupported(SlotDataFlavor.SLOT_DATA_FLAVOR);
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport support) {
        if(!this.canImport(support)) {
            return false;
        }

        Transferable t = support.getTransferable();
        NodeSlotView slot = null;

        try {
            slot = (NodeSlotView)t.getTransferData(SlotDataFlavor.SLOT_DATA_FLAVOR);
        } catch(Exception e) {
            e.printStackTrace();
        }

        if(slot == null) {
            return false;
        }

        DropLocation location = support.getDropLocation();
        ((NodeSlotView)support.getComponent()).onDrop(slot, location.getDropPoint());
        return true;
    }
}
