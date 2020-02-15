package com.visualipcv.editor;

import com.visualipcv.core.Graph;
import com.visualipcv.core.io.GraphStore;
import com.visualipcv.view.ConsoleView;
import com.visualipcv.view.FunctionListView;
import com.visualipcv.view.GraphTabPane;
import com.visualipcv.view.GraphView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
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
    private static GraphTabPane docsPane;
    private static MenuBar menuBar;

    private static Menu getOrCreateSubmenu(Menu menu, String path) {
        String[] items = path.split("/");
        Menu submenu = menu;

        for(int i = 1; i < items.length; i++) {
            Menu temp = null;

            for(MenuItem item : submenu.getItems()) {
                if(item.getText().equals(items[i])) {
                    temp = (Menu)item;
                    break;
                }
            }

            if(temp == null) {
                temp = new Menu(items[i]);
            }

            submenu.getItems().add(temp);

            if(i == items.length - 1) {
                return temp;
            } else {
                submenu = (Menu)temp;
            }
        }

        return submenu;
    }

    private static Menu getOrCreateTopSubmenu(String path) {
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

        return submenu;
    }

    private static MenuItem createMenuItem(Menu menu, String path, EventHandler<ActionEvent> handler, KeyCombination key) {
        Menu m = getOrCreateSubmenu(menu, path.substring(0, path.lastIndexOf('/')));
        String[] items = path.split("/");
        MenuItem item = new MenuItem(items[items.length - 1]);
        item.setOnAction(handler);

        if(key != null)
            item.setAccelerator(key);

        m.getItems().add(item);
        return item;
    }

    private static SeparatorMenuItem createMenuSeparator(Menu menu, String path) {
        Menu m = getOrCreateSubmenu(menu, path);
        SeparatorMenuItem item = new SeparatorMenuItem();
        m.getItems().add(item);
        return item;
    }

    public static void addMenuCommand(String path, EventHandler<ActionEvent> handler, KeyCombination key) {
        createMenuItem(getOrCreateTopSubmenu(path), path, handler, key);
    }

    public static void addMenuSeparator(String path) {
        createMenuSeparator(getOrCreateTopSubmenu(path), path);
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
            }, null);
        }
    }

    private static Tab createTabForGraph(GraphView graph) {
        Tab tab = new Tab("", graph);
        tab.textProperty().bind(graph.getViewModel().getNameProperty());
        return tab;
    }

    private static void createSysMenu() {
        addMenuCommand("File/New", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                getDocsPane().createNew();
            }
        }, new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));

        addMenuCommand("File/Open", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                getDocsPane().load();
            }
        }, new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));

        addMenuCommand("File/Save", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                getDocsPane().save();
            }
        }, new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));

        addMenuCommand("File/Save as", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                getDocsPane().saveAs();
            }
        }, new KeyCodeCombination(KeyCode.S, KeyCombination.SHIFT_DOWN, KeyCombination.CONTROL_DOWN));

        addMenuSeparator("File");

        addMenuCommand("File/Exit", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                getPrimaryStage().close();
            }
        }, null);
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

        docsPane = new GraphTabPane();
        docsPane.createNew();

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

    public static GraphTabPane getDocsPane() {
        return docsPane;
    }

    public static MenuBar getMenuBar() {
        return menuBar;
    }
}
