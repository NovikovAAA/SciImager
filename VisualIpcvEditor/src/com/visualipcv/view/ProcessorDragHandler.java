package com.visualipcv.view;

import com.visualipcv.Processor;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

class ProcessorDragHandler extends TransferHandler {
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
            e.printStackTrace();
            return false;
        }

        GraphView target = (GraphView)support.getComponent();
        DropLocation location = support.getDropLocation();

        NodeView node = new NodeView(p);
        Point point = target.toGraphCoords(location.getDropPoint());
        node.setLocation(point);
        target.add(node);
        return true;
    }
}
