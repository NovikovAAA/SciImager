package com.visualipcv.viewmodel;

import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorLibrary;
import com.visualipcv.view.FunctionListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionListViewModel {
    private FunctionRecord root;

    public FunctionListViewModel() {
        Map<String, List<Processor>> categories = new HashMap<>();

        for(Processor processor : ProcessorLibrary.getProcessors()) {
            if(!categories.containsKey(processor.getCategory())) {
                categories.put(processor.getCategory(), new ArrayList<>());
            }

            categories.get(processor.getCategory()).add(processor);
        }

        root = new FunctionRecord(categories);
    }

    public FunctionRecord getRoot() {
        return root;
    }
}
