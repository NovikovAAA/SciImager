package com.visualipcv.editor;

import com.visualipcv.core.CommonException;
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
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.dockfx.DockNode;
import org.dockfx.DockPane;
import org.dockfx.DockPos;
import org.reflections.Reflections;

import javax.swing.plaf.MenuBarUI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Editor {
    private static Stage primaryStage;
    private static DockPane primaryPane;

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

    private static Menu[] createAppMenu() {
        List<Menu> menus = new ArrayList<>();

        Reflections reflections = new Reflections("com.visualipcv");
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(EditorWindow.class);

        for(Class<?> clazz : classes) {
            String name = clazz.getAnnotation(EditorWindow.class).name();
            String path = clazz.getAnnotation(EditorWindow.class).path();
            DockPos dockPos = clazz.getAnnotation(EditorWindow.class).dockPos();

            String firstItem = path.split("/")[0];
            Menu targetMenu = null;

            targetMenu = menus.stream().filter((Menu menu) -> menu.getText().equals(firstItem)).findFirst().orElse(null);

            if(targetMenu == null) {
                targetMenu = new Menu(firstItem);
                menus.add(targetMenu);
            }

            MenuItem item = createMenuItem(targetMenu, path, new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        DockNode node = new DockNode((Node)clazz.newInstance(), clazz.getAnnotation(EditorWindow.class).name());
                        node.dock(getPrimaryPane(), clazz.getAnnotation(EditorWindow.class).dockPos());
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        return menus.toArray(new Menu[0]);
    }

    public static void initPrimaryStage(Stage stage) {
        Editor.primaryStage = stage;

        primaryStage.setTitle("VisualIPCV");

        VBox root = new VBox();
        root.getChildren().add(new MenuBar(createAppMenu()));

        DockPane dockPane = new DockPane();
        primaryPane = dockPane;
        VBox.setVgrow(dockPane, Priority.ALWAYS);
        root.getChildren().add(dockPane);

        DockNode functionListPanel = new DockNode(new FunctionListView(), "Functions");
        DockNode graphPanel = new DockNode(new GraphView(), "Graph");
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
}
