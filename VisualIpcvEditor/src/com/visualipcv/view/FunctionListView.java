package com.visualipcv.view;

import com.visualipcv.Processor;
import com.visualipcv.ProcessorLibrary;
import com.visualipcv.view.dragdrop.ProcessorDragHandler;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

public class FunctionListView extends JScrollPane {
    private ProcessorLibrary library;
    private JTree list;

    public FunctionListView(ProcessorLibrary library) {
        this.library = library;
        HashMap<String, ArrayList<Processor>> processors = new HashMap<>();

        for(int i = 0; i < library.getProcessors().length; i++) {
            Processor proc = library.getProcessors()[i];
            if(!processors.containsKey(proc.getCategory())) {
                processors.put(proc.getCategory(), new ArrayList<>());
            }
            ArrayList<Processor> procs = processors.get(proc.getCategory());
            procs.add(proc);
        }

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        Set<String> categories = processors.keySet();
        Object[] categoriesArray = categories.toArray();

        Arrays.sort(categoriesArray, (Object v0, Object v1) -> {
            String s0 = (String)v0;
            String s1 = (String)v1;
            return s0.compareTo(s1);
        });

        for(Object category : categoriesArray) {
            DefaultMutableTreeNode categoryNode = new DefaultMutableTreeNode(category);
            root.add(categoryNode);

            for(Processor proc : processors.get((String)category)) {
                DefaultMutableTreeNode procNode = new DefaultMutableTreeNode(proc);
                categoryNode.add(procNode);
            }
        }

        list = new JTree(root);
        list.setRootVisible(false);
        list.setDragEnabled(true);
        list.setTransferHandler(new ProcessorDragHandler());
        setViewportView(list);
    }
}
