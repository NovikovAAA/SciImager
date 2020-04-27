package com.visualipcv.editor;

import com.visualipcv.Console;
import com.visualipcv.controller.DocumentManagerController;
import com.visualipcv.core.DocumentManager;
import com.visualipcv.view.AppScene;
import com.visualipcv.controller.ConsoleController;
import com.visualipcv.controller.Controller;
import com.visualipcv.controller.FunctionListController;
import com.visualipcv.view.NormalStage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import com.visualipcv.view.docking.DockNode;
import com.visualipcv.view.docking.DockPane;
import com.visualipcv.view.docking.DockPos;
import javafx.stage.Stage;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Editor {
    private static Stage primaryStage;
    private static DockPane primaryPane;
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
        if(path == null || path.isEmpty())
            return null;

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
        Reflections reflections = new Reflections("com.visualipcv", new SubTypesScanner(), new TypeAnnotationsScanner(), new MethodAnnotationsScanner());
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(EditorWindow.class);

        for(Class<?> clazz : classes) {
            String name = clazz.getAnnotation(EditorWindow.class).name();
            String path = clazz.getAnnotation(EditorWindow.class).path();
            DockPos dockPos = clazz.getAnnotation(EditorWindow.class).dockPos();

            if(path.isEmpty())
                continue;

            Editor.addMenuCommand(path, new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        openDockedWindow((Controller<?>)clazz.newInstance());
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }, null);
        }

        Set<Method> commands = reflections.getMethodsAnnotatedWith(EditorCommand.class);

        for(Method method : commands) {
            String path = method.getAnnotation(EditorCommand.class).path();

            if(!Modifier.isStatic(method.getModifiers()))
                Console.write("Failed adding editor command '" + path + "': method is not static");

            Editor.addMenuCommand(path, new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        method.invoke(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, null);
        }
    }

    private static void createSysMenu() {
        addMenuCommand("File/New", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DocumentManager.createDocument();
            }
        }, new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));

        addMenuCommand("File/Open", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DocumentManager.loadDocument();
            }
        }, new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));

        addMenuCommand("File/Save", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DocumentManager.saveAll();
            }
        }, new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));

        addMenuCommand("File/Save as", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                throw new RuntimeException("Not implemented");
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

        FunctionListController functionListController = new FunctionListController();
        DockNode functionListPanel = new DockNode(functionListController);

        DocumentManagerController documentManagerController = new DocumentManagerController();
        DockNode documentManagerPanel = new DockNode(documentManagerController);

        ConsoleController consoleController = new ConsoleController();
        DockNode consolePanel = new DockNode(consoleController);

        functionListPanel.dock(dockPane, DockPos.LEFT);
        documentManagerPanel.dock(dockPane, DockPos.BOTTOM);
        consolePanel.dock(dockPane, DockPos.RIGHT);

        primaryStage.setScene(new AppScene(root, 1280, 720));
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

    public static MenuBar getMenuBar() {
        return menuBar;
    }

    public static void openFloatingWindow(Controller<?> controller) {
        if(activateIfExists(controller.getClass(), controller.getContext()))
            return;

        DockNode dockNode = new DockNode(controller);
        dockNode.setFloating(true);
        dockNode.setMaximized(true);
    }

    public static void openDockedWindow(Controller<?> controller) {
        if(activateIfExists(controller.getClass(), controller.getContext()))
            return;

        EditorWindow anno = controller.getClass().getAnnotation(EditorWindow.class);

        if(anno == null)
            throw new RuntimeException("Cannot open window that is not EditorWindow");

        DockPos dockPos = anno.dockPos();

        DockNode node = new DockNode(controller);
        node.dock(getPrimaryPane(), dockPos);
    }

    public static void openWindow(Controller<?> controller) {
        if(activateIfExists(controller.getClass(), controller.getContext()))
            return;

        DockNode node = findDockNodeWithController(controller.getClass());

        if(node != null) {
            node.addTab(controller);
            activateIfExists(controller.getClass(), controller.getContext());
            return;
        }

        openFloatingWindow(controller);
    }

    private static Set<Node> getAllDockNodes() {
        Set<Node> nodes = new HashSet<>();

        for(Stage stage : NormalStage.getStages()) {
            nodes.addAll(stage.getScene().getRoot().lookupAll(".dock-node"));
        }

        return nodes;
    }

    public static DockNode findDockNodeWithController(Class<?> controllerClass) {
        Set<Node> nodes = getAllDockNodes();

        for(Node node : nodes) {
            DockNode dockNode = (DockNode)node;

            for(Tab tab : dockNode.getTabPane().getTabs()) {
                if(dockNode.getController(tab).getClass() == controllerClass)
                    return dockNode;
            }
        }

        return null;
    }

    public static <T> T findOpenedWindow(Class<T> controllerClass) {
        Set<Node> nodes = getAllDockNodes();

        for(Node node : nodes) {
            DockNode dockNode = (DockNode)node;

            for(Tab tab : dockNode.getTabPane().getTabs()) {
                if(dockNode.getController(tab).getClass() == controllerClass)
                    return (T)dockNode.getController(tab);
            }
        }

        return null;
    }

    private static String getWindowName(Controller<?> controller) {
        EditorWindow anno = controller.getClass().getAnnotation(EditorWindow.class);

        if(anno == null)
            throw new RuntimeException("Cannot get name of window because it is not an EditorWindow");

        return anno.name();
    }

    public static boolean activateIfExists(Class<?> controllerClass, Object context) {
        Set<Node> nodes = getAllDockNodes();

        for(Node node : nodes) {
            DockNode dockNode = (DockNode)node;

            for(Tab tab : dockNode.getTabPane().getTabs()) {
                if(dockNode.getController(tab).getClass() == controllerClass &&
                    dockNode.getController(tab).getContext() == context) {
                    dockNode.getTabPane().getSelectionModel().select(tab);
                    dockNode.getTabPane().requestFocus();
                    return true;
                }
            }
        }

        return false;
    }

    public static void closeWindow(Class<?> controllerClass, Object context) {
        Set<Node> nodes = getAllDockNodes();

        for(Node node : nodes) {
            DockNode dockNode = (DockNode)node;

            List<Tab> tabs = new ArrayList<>(dockNode.getTabPane().getTabs());

            for(Tab tab : tabs) {
                if((dockNode.getController(tab).getClass() == controllerClass || controllerClass == null) && dockNode.getController(tab).getContext() == context) {
                    dockNode.closeTab(tab);
                }
            }
        }
    }
}
