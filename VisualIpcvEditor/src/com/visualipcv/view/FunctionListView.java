package com.visualipcv.view;

import com.visualipcv.Processor;
import com.visualipcv.ProcessorLibrary;

import javax.swing.*;

public class FunctionListView extends JScrollPane {
    private ProcessorLibrary library;
    private JList<Processor> list;

    public FunctionListView(ProcessorLibrary library) {
        this.library = library;
        DefaultListModel<Processor> model = new DefaultListModel<>();

        for(int i = 0; i < library.getProcessors().length; i++) {
            model.addElement(library.getProcessors()[i]);
        }

        list = new JList<Processor>(model);
        setViewportView(list);
    }
}
