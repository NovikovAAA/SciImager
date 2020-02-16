package com.visualipcv.view;

import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorLibrary;
import com.visualipcv.editor.EditorWindow;
import com.visualipcv.events.RefreshEventListener;
import com.visualipcv.viewmodel.FunctionListViewModel;
import com.visualipcv.viewmodel.FunctionRecord;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import org.dockfx.DockPos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

@EditorWindow(path = "View/Function list", name = "Function list", dockPos = DockPos.LEFT)
public class FunctionListView extends AnchorPane {
    @FXML
    private TreeView<FunctionRecord> treeView;
    @FXML
    private Button addButton;

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
        treeView.setCellFactory((view) -> {
            TreeCell<FunctionRecord> cell = new TextFieldTreeCell<>();

            cell.setOnDragDetected(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    FunctionRecord record = cell.getItem();

                    if(record.getProcessor() == null)
                        return;

                    Dragboard db = cell.startDragAndDrop(TransferMode.ANY);
                    ClipboardContent content = new ClipboardContent();
                    content.putString(record.getProcessor().getModule() + "/" + record.getProcessor().getName());
                    db.setContent(content);
                    db.setDragView(cell.snapshot(null, null));
                    event.consume();
                }
            });

            return cell;
        });

        init();

        ProcessorLibrary.getInstance().addListener(new RefreshEventListener() {
            @Override
            public void refresh() {
                init();
            }
        });

        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                viewModel.addFunction();
            }
        });
    }

    private void init() {
        viewModel.reload();
        treeView.setRoot(new RecursiveTreeItem<>(viewModel.getRoot(), FunctionRecord::getSubFunctions));
    }
}
