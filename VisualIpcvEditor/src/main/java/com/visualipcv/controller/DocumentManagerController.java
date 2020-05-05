package com.visualipcv.controller;

import com.visualipcv.controller.binding.Binder;
import com.visualipcv.controller.binding.PropertyChangedEventListener;
import com.visualipcv.controller.binding.UIProperty;
import com.visualipcv.controller.scriptconstruction.SciScriptEditor;
import com.visualipcv.core.Document;
import com.visualipcv.core.DocumentManager;
import com.visualipcv.core.Graph;
import com.visualipcv.core.IDocumentPart;
import com.visualipcv.core.events.RefreshEventListener;
import com.visualipcv.editor.Editor;
import com.visualipcv.editor.EditorWindow;
import com.visualipcv.scripts.SciScript;
import com.visualipcv.view.docking.DockPos;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.util.HashSet;
import java.util.Set;

@EditorWindow(path = "View/Document manager", name = "Document manager", dockPos = DockPos.LEFT, prefWidth = 300.0, prefHeight = 500.0)
public class DocumentManagerController extends Controller<AnchorPane> {
    private static class DocumentElement {
        private String name;
        private Object object;

        public DocumentElement(IDocumentPart part) {
            this.name = part.getName();
            this.object = part;
        }

        public String getName() {
            return name;
        }

        public Graph getGraph() {
            return (Graph)object;
        }

        public SciScript getScript() {
            return (SciScript)object;
        }

        public Document getDocument() {
            return (Document)object;
        }

        public IDocumentPart getDocumentPart() {
            return (IDocumentPart)object;
        }

        public boolean isGraph() {
            return object instanceof Graph;
        }

        public boolean isScript() {
            return object instanceof SciScript;
        }

        public boolean isDocument() {
            return object instanceof Document;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static class DocumentCell extends TextFieldTreeCell<DocumentElement> {
        public DocumentCell() {
            setConverter(new StringConverter<DocumentElement>() {
                @Override
                public String toString(DocumentElement object) {
                    return object.toString();
                }

                @Override
                public DocumentElement fromString(String string) {
                    getItem().getDocumentPart().setName(string);
                    getTreeView().setEditable(false);
                    return new DocumentElement(getItem().getDocumentPart());
                }
            });
        }

        @Override
        public void updateItem(DocumentElement document, boolean empty) {
            super.updateItem(document, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                setText(document == null ? "" : document.getName());
                setGraphic(getTreeItem().getGraphic());

                if (document == null)
                    return;

                if (document.isGraph()) {
                    setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                                GraphController controller = new GraphController(document.getGraph());
                                Editor.openWindow(controller, StartPageController.class);
                                event.consume();
                            }
                        }
                    });
                } else if (document.isScript()) {
                    setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                                SciScriptEditor editor = new SciScriptEditor(document.getScript());
                                Editor.openWindow(editor, StartPageController.class);
                                event.consume();
                            }
                        }
                    });
                }

                if (document.isDocument()) {
                    setContextMenu(createDocumentContextMenu(document));
                } else {
                    setContextMenu(createDocumentPartContextMenu(this, document));
                }
            }
        }
    }

    private static ContextMenu createDocumentPartContextMenu(DocumentCell cell, DocumentElement document) {
        ContextMenu menu = new ContextMenu();

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                IDocumentPart part = document.getDocumentPart();
                part.getDocument().remove(part);
            }
        });

        MenuItem renameItem = new MenuItem("Rename");
        renameItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cell.getTreeView().setEditable(true);
                cell.startEdit();
            }
        });

        menu.getItems().add(renameItem);
        menu.getItems().add(deleteItem);
        return menu;
    }

    private static ContextMenu createDocumentContextMenu(DocumentElement document) {
        ContextMenu menu = new ContextMenu();

        MenuItem newGraphItem = new MenuItem("New graph");
        newGraphItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                document.getDocument().addGraph();
            }
        });

        MenuItem newScriptItem = new MenuItem("New script");
        newScriptItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                document.getDocument().addScript();
            }
        });

        MenuItem closeItem = new MenuItem("Close");
        closeItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DocumentManager.closeDocument(document.getDocument());
            }
        });

        menu.getItems().add(newGraphItem);
        menu.getItems().add(newScriptItem);
        menu.getItems().add(closeItem);

        return menu;
    }

    private TreeView<DocumentElement> treeView;
    private UIProperty documentsProperty = new UIProperty();

    public DocumentManagerController() {
        super(AnchorPane.class);

        treeView = new TreeView<>();
        treeView.setShowRoot(false);

        treeView.setCellFactory(new Callback<TreeView<DocumentElement>, TreeCell<DocumentElement>>() {
            @Override
            public TreeCell<DocumentElement> call(TreeView<DocumentElement> param) {
                DocumentCell cell =  new DocumentCell();

                cell.setOnDragDetected(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {

                    }
                });

                return cell;
            }
        });

        documentsProperty.setBinder(new Binder() {
            @Override
            public Object update(Object context) {
                return new HashSet<>(DocumentManager.getDocuments());
            }
        });

        documentsProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                rebuildTree((Set<Document>)oldValue, (Set<Document>)newValue);
            }
        });

        initialize();

        DocumentManager.getInstance().addListener(new RefreshEventListener() {
            @Override
            public void refresh() {
                invalidate();
            }
        });

        getView().getChildren().add(treeView);
        AnchorPane.setRightAnchor(treeView, 5.0);
        AnchorPane.setLeftAnchor(treeView, 5.0);
        AnchorPane.setTopAnchor(treeView, 5.0);
        AnchorPane.setBottomAnchor(treeView, 5.0);

        invalidate();
    }

    private void rebuildTree(Set<Document> oldList, Set<Document> documents) {
        TreeItem<DocumentElement> root = new TreeItem<>();

        for(Document document : documents) {
            TreeItem<DocumentElement> documentItem = new TreeItem<>(new DocumentElement(document));
            root.getChildren().add(documentItem);
            documentItem.setExpanded(true);

            for(IDocumentPart part : document.getParts()) {
                TreeItem<DocumentElement> item = new TreeItem<>(new DocumentElement(part));
                documentItem.getChildren().add(item);
            }
        }

        treeView.setRoot(root);
    }
}
