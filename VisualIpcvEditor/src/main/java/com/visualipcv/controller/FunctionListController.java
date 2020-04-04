package com.visualipcv.controller;

import com.visualipcv.controller.scriptconstruction.SciScriptEditor;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorLibrary;
import com.visualipcv.editor.Editor;
import com.visualipcv.editor.EditorWindow;
import com.visualipcv.events.RefreshEventListener;
import com.visualipcv.view.RecursiveTreeItem;
import com.visualipcv.view.FunctionRecord;
import com.visualipcv.view.SearchListView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import com.visualipcv.view.docking.DockPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@EditorWindow(path = "View/Function list", name = "Function list", dockPos = DockPos.LEFT)
public class FunctionListController extends Controller<AnchorPane> {
    @FXML
    private SearchListView<FunctionRecord> treeView;
    @FXML
    private Button addButton;

    public FunctionListController() {
        super(AnchorPane.class, "FunctionListView.fxml");

        treeView.getTree().setCellFactory((view) -> {
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

        invalidate();

        ProcessorLibrary.getInstance().addListener(new RefreshEventListener() {
            @Override
            public void refresh() {
                invalidate();
            }
        });

        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Editor.openWindow(new SciScriptEditor(), "Script");
            }
        });
    }

    @Override
    public void invalidate() {
        super.invalidate();
        HashMap<String, List<Processor>> categories = new HashMap<>();

        for(Processor processor : ProcessorLibrary.getProcessors()) {
            if(!categories.containsKey(processor.getCategory())) {
                categories.put(processor.getCategory(), new ArrayList<>());
            }

            FunctionRecord func = new FunctionRecord(processor);
            categories.get(processor.getCategory()).add(processor);
        }

        treeView.setRoot(new RecursiveTreeItem<FunctionRecord>(new FunctionRecord(categories), FunctionRecord::getSubFunctions));
    }
}
