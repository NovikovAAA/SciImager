package com.visualipcv.view;

import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorLibrary;
import com.visualipcv.viewmodel.FunctionListViewModel;
import com.visualipcv.viewmodel.FunctionRecord;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

public class FunctionListView extends AnchorPane {
    @FXML
    private TreeView<FunctionRecord> treeView;

    private FunctionListViewModel viewModel = new FunctionListViewModel();

    public FunctionListView() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("FunctionListView.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch(IOException e) {
            e.printStackTrace();
        }

        treeView.setShowRoot(false);
        treeView.setRoot(new RecursiveTreeItem<>(viewModel.getRoot(), FunctionRecord::getSubFunctions));
    }
}
