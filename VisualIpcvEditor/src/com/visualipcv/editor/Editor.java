package com.visualipcv.editor;

import com.visualipcv.core.io.GraphStore;
import com.visualipcv.view.ConsoleView;
import com.visualipcv.view.FunctionListView;
import com.visualipcv.view.GraphView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.dockfx.DockNode;
import org.dockfx.DockPane;
import org.dockfx.DockPos;
import org.reflections.Reflections;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Editor {
    private static Stage primaryStage;
    private static DockPane primaryPane;
    private static TabPane docsPane;
    private static MenuBar menuBar;

    private static MenuItem createMenuItem(Menu menu, String path, EventHandler<ActionEvent> handler) {
        String[] items = path.split("/");
        Menu submenu = menu;

        for(int i = 1; i < items.length; i++) {
            MenuItem temp = null;

            for(MenuItem item : submenu.getItems()) {
                if(item.getText().equals(items[i])) {
                    temp = item;
                    break;
                }
            }

            if(temp == null) {
                if(i == items.length - 1)
                    temp = new MenuItem(items[i]);
                else
                    temp = new Menu(items[i]);
            }

            if(i < items.length - 1 && !(temp instanceof Menu)) {
                System.out.println("Failed menu initialization for path: " + path);
            }

            temp.setOnAction(handler);
            submenu.getItems().add(temp);

            if(i == items.length - 1) {
                return temp;
            } else {
                submenu = (Menu)temp;
            }
        }

        return null;
    }

    public static void addMenuCommand(String path, EventHandler<ActionEvent> handler) {
        String[] items = path.split("/");
        Menu submenu = null;

        for(Menu menu : menuBar.getMenus()) {
            if(menu.getText().equals(items[0])) {
                submenu = menu;
                break;
            }
        }

        if(submenu == null) {
            submenu = new Menu(items[0]);
            menuBar.getMenus().add(submenu);
        }

        createMenuItem(submenu, path, handler);
    }

    private static void createAppMenu() {
        Reflections reflections = new Reflections("com.visualipcv");
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(EditorWindow.class);

        for(Class<?> clazz : classes) {
            String name = clazz.getAnnotation(EditorWindow.class).name();
            String path = clazz.getAnnotation(EditorWindow.class).path();
            DockPos dockPos = clazz.getAnnotation(EditorWindow.class).dockPos();

            Editor.addMenuCommand(path, new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        DockNode node = new DockNode((Node)clazz.newInstance(), name);
                        node.dock(getPrimaryPane(), dockPos);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private static void createSysMenu() {
        addMenuCommand("File/New", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                getDocsPane().getTabs().add(new Tab("Graph", new GraphView()));
            }
        });

        addMenuCommand("File/Open", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser chooser = new FileChooser();
                chooser.setTitle("Open");
                chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("VisualIPCV graph", ".vip"));
                File file = chooser.showOpenDialog(Editor.getPrimaryStage());

                try {
                    if(file != null) {
                        GraphView view = new GraphView();
                        view.load(file.getAbsolutePath());
                        getDocsPane().getTabs().add(new Tab("Graph", view));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        addMenuCommand("File/Save as", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser chooser = new FileChooser();
                chooser.setTitle("Save");
                chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("VisualIPCV graph", ".vip"));
                File file = chooser.showSaveDialog(Editor.getPrimaryStage());

                try {
                    if(file != null) {
                        GraphView view = (GraphView)getDocsPane().getSelectionModel().getSelectedItem().getContent();

                        if(view != null) {
                            view.save(file.getAbsolutePath());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        addMenuCommand("File/Exit", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                getPrimaryStage().close();
            }
        });
    }

    public static void initPrimaryStage(Stage stage) {
        Editor.primaryStage = stage;

        primaryStage.setTitle("VisualIPCV");

        VBox root = new VBox();
        menuBar = new MenuBar();
        root.getChildren().add(menuBar);

        createSysMenu();
        createAppMenu();

        DockPane dockPane = new DockPane();
        primaryPane = dockPane;
        VBox.setVgrow(dockPane, Priority.ALWAYS);
        root.getChildren().add(dockPane);

        docsPane = new TabPane();
        docsPane.getTabs().add(new Tab("Graph", new GraphView()));

        DockNode functionListPanel = new DockNode(new FunctionListView(), "Functions");
        DockNode graphPanel = new DockNode(docsPane, "Graph");
        DockNode consolePanel = new DockNode(new ConsoleView(), "Console");

        graphPanel.setDockTitleBar(null);
        graphPanel.dock(dockPane, DockPos.CENTER);
        consolePanel.dock(dockPane, DockPos.BOTTOM);
        functionListPanel.dock(dockPane, DockPos.LEFT);

        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.sizeToScene();
        primaryStage.show();

        DockPane.initializeDefaultUserAgentStylesheet();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static DockPane getPrimaryPane() {
        return primaryPane;
    }

    public static TabPane getDocsPane() {
        return docsPane;
    }

    public static MenuBar getMenuBar() {
        return menuBar;
    }
}
