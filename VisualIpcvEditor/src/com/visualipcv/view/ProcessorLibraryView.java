package com.visualipcv.view;

import com.visualipcv.Processor;
import com.visualipcv.ProcessorLibrary;

import javax.swing.*;
import java.awt.*;

public class ProcessorLibraryView extends JPanel {
    private JList<Processor> list;
    private JScrollPane scrollPane;
    private SpringLayout layout;

    private ProcessorLibrary library;
    private int padding = 5;

    public ProcessorLibraryView(ProcessorLibrary library) {
        layout = new SpringLayout();
        list = new JList<>();
        scrollPane = new JScrollPane(list);

        setLayout(layout);
        add(scrollPane);
        setPadding(10);

        setLibrary(library);
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
        layout.putConstraint(SpringLayout.WEST, scrollPane, padding,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.EAST, scrollPane, -padding,
                SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.NORTH, scrollPane, padding,
                SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.SOUTH, scrollPane, -padding,
                SpringLayout.SOUTH, this);
    }

    public ProcessorLibrary getLibrary() {
        return library;
    }

    public void setLibrary(ProcessorLibrary library) {
        this.library = library;
        this.list.setListData(this.library.getProcessors());
    }
}
