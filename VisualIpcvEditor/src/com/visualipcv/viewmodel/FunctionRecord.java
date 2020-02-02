package com.visualipcv.viewmodel;

import com.visualipcv.core.Processor;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class FunctionRecord {
    private StringProperty name = new SimpleStringProperty();
    private Processor processor;
    private ObservableList<FunctionRecord> subFunctions = FXCollections.observableArrayList();

    public FunctionRecord(Map<String, List<Processor>> processors) {
        for(String category : processors.keySet()) {
            subFunctions.addAll(new FunctionRecord(category, processors.get(category)));
        }
        subFunctions.sort(new Comparator<FunctionRecord>() {
            @Override
            public int compare(FunctionRecord o1, FunctionRecord o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
    }

    public FunctionRecord(Processor processor) {
        this.processor = processor;
        name.setValue(processor.getName());
    }

    public FunctionRecord(String category, List<Processor> processorList) {
        name.setValue(category);

        for(Processor record : processorList) {
            subFunctions.add(new FunctionRecord(record));
        }
    }

    public String getName() {
        return name.getValue();
    }

    public Processor getProcessor() {
        return processor;
    }

    public ObservableList<FunctionRecord> getSubFunctions() {
        return subFunctions;
    }

    @Override
    public String toString() {
        return name.getValue();
    }
}
