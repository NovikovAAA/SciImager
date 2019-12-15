package com.visualipcv.view.dragdrop;

import com.visualipcv.Processor;
import com.visualipcv.view.GraphView;
import com.visualipcv.view.NodeView;
import com.visualipcv.view.dragdrop.ProcessorDataFlavor;
import com.visualipcv.view.dragdrop.ProcessorTransferable;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.datatransfer.Transferable;

public class ProcessorDragHandler extends TransferHandler {
    @Override
    public int getSourceActions(JComponent c) {
        return TransferHandler.COPY_OR_MOVE;
    }

    @Override
    public Transferable createTransferable(JComponent source) {
        JTree tree = (JTree) source;
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();

        if (!(node.getUserObject() instanceof Processor))
            return null;

        return new ProcessorTransferable((Processor)node.getUserObject());
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport support) {
        if(!support.isDrop()) {
            return false;
        }
        if(!(support.getComponent() instanceof GraphView)) {
            return false;
        }
        return support.isDataFlavorSupported(ProcessorDataFlavor.PROCESSOR_DATA_FLAVOR);
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport support) {
        if(!this.canImport(support)) {
            return false;
        }

        Transferable t = support.getTransferable();
        Processor p = null;

        try {
            p = (Processor)t.getTransferData(ProcessorDataFlavor.PROCESSOR_DATA_FLAVOR);
        } catch(Exception e) {
            return false;
        }

        GraphView target = (GraphView)support.getComponent();
        DropLocation location = support.getDropLocation();
        Point point = target.toGraphCoords(location.getDropPoint());
        target.onDrop(p, point);
        return true;
    }
}
