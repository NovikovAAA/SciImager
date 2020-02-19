package com.visualipcv.view.docking;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.css.PseudoClass;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class DockNode extends VBox implements EventHandler<MouseEvent> {

    private abstract class EventTask {
        protected int executions = 0;

        public abstract void run(Node node, Node dragNode);

        public int getExecutions() {
            return executions;
        }

        public void reset() {
            executions = 0;
        }
    }

    private StageStyle stageStyle = StageStyle.TRANSPARENT;

    private Stage stage;

    private Point2D dragStart;
    private boolean dragging = false;
    private HashMap<Window, Node> dragNodes = new HashMap<Window, Node>();
    private DockNodeMoveEventHandler moveEventHandler = new DockNodeMoveEventHandler();

    private TabPane tabPane;
    private DockPane dockPane;
    private static final PseudoClass FLOATING_PSEUDO_CLASS = PseudoClass.getPseudoClass("floating");
    private static final PseudoClass DOCKED_PSEUDO_CLASS = PseudoClass.getPseudoClass("docked");
    private static final PseudoClass MAXIMIZED_PSEUDO_CLASS = PseudoClass.getPseudoClass("maximized");

    private BooleanProperty maximizedProperty = new SimpleBooleanProperty(false) {

        @Override
        protected void invalidated() {
            DockNode.this.pseudoClassStateChanged(MAXIMIZED_PSEUDO_CLASS, get());
            if (getBorderPane() != null) {
                getBorderPane().pseudoClassStateChanged(MAXIMIZED_PSEUDO_CLASS, get());
            }

            stage.setMaximized(get());

            if (this.get()) {
                Screen screen = Screen
                        .getScreensForRectangle(stage.getX(), stage.getY(), stage.getWidth(), stage.getHeight())
                        .get(0);
                Rectangle2D bounds = screen.getVisualBounds();

                stage.setX(bounds.getMinX());
                stage.setY(bounds.getMinY());

                stage.setWidth(bounds.getWidth());
                stage.setHeight(bounds.getHeight());
            }
        }

        @Override
        public String getName() {
            return "maximized";
        }
    };

    public DockNode(Node contents, String title, Node graphic) {
        this.titleProperty.setValue(title);
        this.graphicProperty.setValue(graphic);

        tabPane = new TabPane();
        getChildren().add(tabPane);
        VBox.setVgrow(tabPane, Priority.ALWAYS);

        addTab(contents, title);
        this.getStyleClass().add("dock-node");

        tabPane.setOnMousePressed(moveEventHandler);
        tabPane.setOnDragDetected(moveEventHandler);
        tabPane.setOnMouseDragged(moveEventHandler);
        tabPane.setOnMouseReleased(moveEventHandler);
    }

    public DockNode(Node contents, String title) {
        this(contents, title, null);
    }

    public DockNode(Node contents) {
        this(contents, null, null);
    }

    public void setStageStyle(StageStyle stageStyle) {
        this.stageStyle = stageStyle;
    }

    public void setContents(Node contents) {
        throw new NotImplementedException();
    }

    public final void setMaximized(boolean maximized) {
        maximizedProperty.set(maximized);
    }

    private Stage createStage(Point2D translation) {
        Point2D floatScene = this.localToScene(0, 0);
        Point2D floatScreen = this.localToScreen(0, 0);

        stage = new Stage();
        stage.titleProperty().bind(titleProperty);

        if (dockPane != null && dockPane.getScene() != null
                && dockPane.getScene().getWindow() != null) {
            stage.initOwner(dockPane.getScene().getWindow());
        }

        stage.initStyle(stageStyle);

        Point2D stagePosition;

        if (this.isDecorated()) {
            Window owner = stage.getOwner();
            stagePosition = floatScene.add(new Point2D(owner.getX(), owner.getY()));
        } else {
            stagePosition = floatScreen;
        }
        if (translation != null) {
            stagePosition = stagePosition.add(translation);
        }

        BorderPane borderPane = new BorderPane();
        borderPane.getStyleClass().add("dock-node-border");
        DockPane dockPane = new DockPane();
        borderPane.setCenter(dockPane);
        Scene scene = new Scene(borderPane);

        borderPane.applyCss();
        Insets insetsDelta = borderPane.getInsets();

        double insetsWidth = insetsDelta.getLeft() + insetsDelta.getRight();
        double insetsHeight = insetsDelta.getTop() + insetsDelta.getBottom();

        stage.setX(stagePosition.getX() - insetsDelta.getLeft());
        stage.setY(stagePosition.getY() - insetsDelta.getTop());

        stage.setMinWidth(borderPane.minWidth(this.getHeight()) + insetsWidth);
        stage.setMinHeight(borderPane.minHeight(this.getWidth()) + insetsHeight);

        dockPane.setPrefSize(this.getWidth() + insetsWidth, this.getHeight() + insetsHeight);
        borderPane.setPrefSize(this.getWidth() + insetsWidth, this.getHeight() + insetsHeight);

        stage.setScene(scene);

        if (stageStyle == StageStyle.TRANSPARENT) {
            scene.setFill(null);
        }

        stage.setResizable(this.isStageResizable());
        stage.sizeToScene();
        stage.show();

        return stage;
    }

    public void setFloating(boolean floating, Point2D translation) {
        if (floating && !this.isFloating()) {
            if(dockPane != null && dockPane.isLastDockNode(this))
                return;

            Stage stage = createStage(translation);
            this.floatingProperty.set(floating);

            this.applyCss();

            if (this.isDocked()) {
                this.undock();
            }

            DockPane pane = ((DockPane)((BorderPane)stage.getScene().getRoot()).getCenter());
            dock(pane, DockPos.CENTER);

            if (this.isStageResizable()) {
                stage.addEventFilter(MouseEvent.MOUSE_PRESSED, this);
                stage.addEventFilter(MouseEvent.MOUSE_MOVED, this);
                stage.addEventFilter(MouseEvent.MOUSE_DRAGGED, this);
            }
        } else if (!floating && this.isFloating()) {
            this.floatingProperty.set(floating);

            stage.removeEventFilter(MouseEvent.MOUSE_PRESSED, this);
            stage.removeEventFilter(MouseEvent.MOUSE_MOVED, this);
            stage.removeEventFilter(MouseEvent.MOUSE_DRAGGED, this);

            stage.close();
        }
    }

    public void setFloating(boolean floating) {
        setFloating(floating, null);
    }

    public final DockPane getDockPane() {
        return dockPane;
    }

    public final Stage getStage() {
        return stage;
    }

    private final BorderPane getBorderPane() {
        if(stage == null)
            return null;
        return (BorderPane)stage.getScene().getRoot();
    }

    public final ObjectProperty<Node> graphicProperty() {
        return graphicProperty;
    }

    private ObjectProperty<Node> graphicProperty = new SimpleObjectProperty<Node>() {
        @Override
        public String getName() {
            return "graphic";
        }
    };

    public final Node getGraphic() {
        return graphicProperty.get();
    }

    public final void setGraphic(Node graphic) {
        this.graphicProperty.setValue(graphic);
    }

    public final StringProperty titleProperty() {
        return titleProperty;
    }

    private StringProperty titleProperty = new SimpleStringProperty("Dock") {
        @Override
        public String getName() {
            return "title";
        }
    };

    public final String getTitle() {
        return titleProperty.get();
    }

    public final void setTitle(String title) {
        this.titleProperty.setValue(title);
    }

    public final BooleanProperty floatingProperty() {
        return floatingProperty;
    }

    private BooleanProperty floatingProperty = new SimpleBooleanProperty(false) {
        @Override
        protected void invalidated() {
            DockNode.this.pseudoClassStateChanged(FLOATING_PSEUDO_CLASS, get());
            if (getBorderPane() != null) {
                getBorderPane().pseudoClassStateChanged(FLOATING_PSEUDO_CLASS, get());
            }
        }

        @Override
        public String getName() {
            return "floating";
        }
    };

    public final boolean isFloating() {
        return floatingProperty.get();
    }

    public final BooleanProperty floatableProperty() {
        return floatableProperty;
    }

    private BooleanProperty floatableProperty = new SimpleBooleanProperty(true) {
        @Override
        public String getName() {
            return "floatable";
        }
    };

    public final boolean isFloatable() {
        return floatableProperty.get();
    }

    public final void setFloatable(boolean floatable) {
        if (!floatable && this.isFloating()) {
            this.setFloating(false);
        }
        this.floatableProperty.set(floatable);
    }

    public final BooleanProperty closableProperty() {
        return closableProperty;
    }

    private BooleanProperty closableProperty = new SimpleBooleanProperty(true) {
        @Override
        public String getName() {
            return "closable";
        }
    };

    public final boolean isClosable() {
        return closableProperty.get();
    }

    public final void setClosable(boolean closable) {
        this.closableProperty.set(closable);
    }

    public final BooleanProperty resizableProperty() {
        return stageResizableProperty;
    }

    private BooleanProperty stageResizableProperty = new SimpleBooleanProperty(true) {
        @Override
        public String getName() {
            return "resizable";
        }
    };

    public final boolean isStageResizable() {
        return stageResizableProperty.get();
    }

    public final void setStageResizable(boolean resizable) {
        stageResizableProperty.set(resizable);
    }

    public final BooleanProperty dockedProperty() {
        return dockedProperty;
    }

    private BooleanProperty dockedProperty = new SimpleBooleanProperty(false) {
        @Override
        protected void invalidated() {
            DockNode.this.pseudoClassStateChanged(DOCKED_PSEUDO_CLASS, get());
        }

        @Override
        public String getName() {
            return "docked";
        }
    };

    public final boolean isDocked() {
        return dockedProperty.get();
    }

    public final BooleanProperty maximizedProperty() {
        return maximizedProperty;
    }

    public final boolean isMaximized() {
        return maximizedProperty.get();
    }

    public final boolean isDecorated() {
        return stageStyle != StageStyle.TRANSPARENT && stageStyle != StageStyle.UNDECORATED;
    }

    public void dock(DockPane dockPane, DockPos dockPos, Node sibling) {
        dockImpl(dockPane);
        dockPane.dock(this, dockPos, sibling);
    }

    public void addTab(Node content, String title) {
        Tab tab = new Tab(title, content);

        tab.setOnClosed(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                if(tabPane.getTabs().isEmpty())
                    DockNode.this.close();
            }
        });

        tabPane.getTabs().add(tab);
    }

    public DockNode floatTab(Tab tab) {
        Stage stage = createStage(null);
        tabPane.getTabs().remove(tab);
        DockNode newDockNode = new DockNode(tab.getContent(), tab.getText());
        newDockNode.floatingProperty.set(true);

        newDockNode.stage = stage;
        DockPane pane = (DockPane)((BorderPane)stage.getScene().getRoot()).getCenter();
        newDockNode.dock(pane, DockPos.CENTER);

        if (newDockNode.isStageResizable()) {
            stage.addEventFilter(MouseEvent.MOUSE_PRESSED, newDockNode);
            stage.addEventFilter(MouseEvent.MOUSE_MOVED, newDockNode);
            stage.addEventFilter(MouseEvent.MOUSE_DRAGGED, newDockNode);
        }

        return newDockNode;
    }

    public void merge(DockNode dockNode) {
        for(Tab tab : dockNode.tabPane.getTabs()) {
            addTab(tab.getContent(), tab.getText());
        }
        dockNode.close();
    }

    public void dock(DockPane dockPane, DockPos dockPos) {
        dockImpl(dockPane);
        dockPane.dock(this, dockPos);
    }

    private final void dockImpl(DockPane dockPane) {
        if (isFloating()) {
            setFloating(false);
        }
        this.dockPane = dockPane;
        this.dockedProperty.set(true);
    }

    public void undock() {
        if (dockPane != null) {
            dockPane.undock(this);
        }
        this.dockedProperty.set(false);
    }

    public void close() {
        if (isFloating()) {
            setFloating(false);
        } else if (isDocked()) {
            undock();
        }
    }

    private Point2D sizeLast;
    private boolean sizeWest = false, sizeEast = false, sizeNorth = false, sizeSouth = false;

    public boolean isMouseResizeZone() {
        return sizeWest || sizeEast || sizeNorth || sizeSouth;
    }

    private void handleResize(MouseEvent event) {
        Cursor cursor = Cursor.DEFAULT;

        if (!this.isFloating() || !this.isStageResizable()) {
            return;
        }

        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            sizeLast = new Point2D(event.getScreenX(), event.getScreenY());
        } else if (event.getEventType() == MouseEvent.MOUSE_MOVED) {
            Insets insets = getBorderPane().getPadding();

            sizeWest = event.getX() < insets.getLeft();
            sizeEast = event.getX() > getBorderPane().getWidth() - insets.getRight();
            sizeNorth = event.getY() < insets.getTop();
            sizeSouth = event.getY() > getBorderPane().getHeight() - insets.getBottom();

            if (sizeWest) {
                if (sizeNorth) {
                    cursor = Cursor.NW_RESIZE;
                } else if (sizeSouth) {
                    cursor = Cursor.SW_RESIZE;
                } else {
                    cursor = Cursor.W_RESIZE;
                }
            } else if (sizeEast) {
                if (sizeNorth) {
                    cursor = Cursor.NE_RESIZE;
                } else if (sizeSouth) {
                    cursor = Cursor.SE_RESIZE;
                } else {
                    cursor = Cursor.E_RESIZE;
                }
            } else if (sizeNorth) {
                cursor = Cursor.N_RESIZE;
            } else if (sizeSouth) {
                cursor = Cursor.S_RESIZE;
            }

            this.getScene().setCursor(cursor);
        } else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED && this.isMouseResizeZone()) {
            Point2D sizeCurrent = new Point2D(event.getScreenX(), event.getScreenY());
            Point2D sizeDelta = sizeCurrent.subtract(sizeLast);

            double newX = stage.getX(), newY = stage.getY(), newWidth = stage.getWidth(),
                    newHeight = stage.getHeight();

            if (sizeNorth) {
                newHeight -= sizeDelta.getY();
                newY += sizeDelta.getY();
            } else if (sizeSouth) {
                newHeight += sizeDelta.getY();
            }

            if (sizeWest) {
                newWidth -= sizeDelta.getX();
                newX += sizeDelta.getX();
            } else if (sizeEast) {
                newWidth += sizeDelta.getX();
            }

            double currentX = sizeLast.getX(), currentY = sizeLast.getY();
            if (newWidth >= stage.getMinWidth()) {
                stage.setX(newX);
                stage.setWidth(newWidth);
                currentX = sizeCurrent.getX();
            }

            if (newHeight >= stage.getMinHeight()) {
                stage.setY(newY);
                stage.setHeight(newHeight);
                currentY = sizeCurrent.getY();
            }
            sizeLast = new Point2D(currentX, currentY);

            if (sizeNorth || sizeSouth || sizeWest || sizeEast) {
                event.consume();
            }
        }
    }

    @Override
    public void handle(MouseEvent event) {
        handleResize(event);
    }

    private class DockNodeMoveEventHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            if(event.getEventType() == MouseEvent.MOUSE_PRESSED) {
                dragStart = new Point2D(event.getX(), event.getY());
            } else if(event.getEventType() == MouseEvent.DRAG_DETECTED) {
                if(event.getY() < tabPane.getTabMaxHeight()) {
                    return;
                }

                if(!isFloating()) {
                    if(tabPane.getTabs().size() == 1) {
                        setFloating(true);

                        if(dockPane != null) {
                            dockPane.addEventFilter(MouseEvent.MOUSE_DRAGGED, this);
                            dockPane.addEventFilter(MouseEvent.MOUSE_RELEASED, this);
                        }
                    } else {
                        DockNode dockNode = floatTab(tabPane.getSelectionModel().getSelectedItem());

                        if(dockPane != null) {
                            dockPane.addEventFilter(MouseEvent.MOUSE_DRAGGED, dockNode.moveEventHandler);
                            dockPane.addEventFilter(MouseEvent.MOUSE_RELEASED, dockNode.moveEventHandler);
                        }

                        dockNode.dragStart = dragStart;
                        dockNode.dragging = true;
                    }
                } else if(isMaximized()) {
                    double ratioX = event.getX() / getWidth();
                    double ratioY = event.getY() / getHeight();

                    setMaximized(false);

                    dragStart = new Point2D(ratioX * getWidth(), ratioY * getHeight());
                }

                if(isFloating())
                    dragging = true;

                event.consume();
            } else if(event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                if(!dragging)
                    return;

                Stage stage = getStage();
                Insets insetsDelta = getBorderPane() != null ? getBorderPane().getInsets() : new Insets(0.0);
                stage.setX(event.getScreenX() - dragStart.getX() - insetsDelta.getLeft());
                stage.setY(event.getScreenY() - dragStart.getY() - insetsDelta.getTop());

                DockEvent dockEnterEvent =
                        new DockEvent(this, DockEvent.NULL_SOURCE_TARGET, DockEvent.DOCK_ENTER, event.getX(),
                                event.getY(), event.getScreenX(), event.getScreenY(), null);
                DockEvent dockOverEvent =
                        new DockEvent(this, DockEvent.NULL_SOURCE_TARGET, DockEvent.DOCK_OVER, event.getX(),
                                event.getY(), event.getScreenX(), event.getScreenY(), null);
                DockEvent dockExitEvent =
                        new DockEvent(this, DockEvent.NULL_SOURCE_TARGET, DockEvent.DOCK_EXIT, event.getX(),
                                event.getY(), event.getScreenX(), event.getScreenY(), null);

                EventTask eventTask = new EventTask() {
                    @Override
                    public void run(Node node, Node dragNode) {
                        executions++;

                        if (dragNode != node) {
                            Event.fireEvent(node, dockEnterEvent.copyFor(DockNode.this, node));

                            if (dragNode != null) {
                                Event.fireEvent(dragNode, dockExitEvent.copyFor(DockNode.this, dragNode));
                            }

                            dragNodes.put(node.getScene().getWindow(), node);
                        }
                        Event.fireEvent(node, dockOverEvent.copyFor(DockNode.this, node));
                    }
                };

                DockNode.this.pickEventTarget(new Point2D(event.getScreenX(), event.getScreenY()), eventTask, dockExitEvent);
            } else if(event.getEventType() == MouseEvent.MOUSE_RELEASED) {
                dragging = false;

                DockEvent dockReleasedEvent =
                        new DockEvent(this, DockEvent.NULL_SOURCE_TARGET, DockEvent.DOCK_RELEASED, event.getX(),
                                event.getY(), event.getScreenX(), event.getScreenY(), null, DockNode.this);

                EventTask eventTask = new EventTask() {
                    @Override
                    public void run(Node node, Node dragNode) {
                        executions++;
                        if (dragNode != node) {
                            Event.fireEvent(node, dockReleasedEvent.copyFor(DockNode.this, node));
                        }
                        Event.fireEvent(node, dockReleasedEvent.copyFor(DockNode.this, node));
                    }
                };

                DockNode.this.pickEventTarget(new Point2D(event.getScreenX(), event.getScreenY()), eventTask, null);
                dragNodes.clear();

                DockPane dockPane = getDockPane();

                if (dockPane != null) {
                    dockPane.removeEventFilter(MouseEvent.MOUSE_DRAGGED, this);
                    dockPane.removeEventFilter(MouseEvent.MOUSE_RELEASED, this);
                }
            }
        }
    }

    private void pickEventTarget(Point2D location, EventTask eventTask, Event explicit) {
        List<DockPane> dockPanes = DockPane.dockPanes;

        for (DockPane dockPane : dockPanes) {
            Window window = dockPane.getScene().getWindow();
            if (!(window instanceof Stage)) continue;
            Stage targetStage = (Stage) window;

            if (targetStage == getStage())
                continue;

            eventTask.reset();

            Node dragNode = dragNodes.get(targetStage);

            Parent root = targetStage.getScene().getRoot();
            Stack<Parent> stack = new Stack<Parent>();
            if (root.contains(root.screenToLocal(location.getX(), location.getY()))
                    && !root.isMouseTransparent()) {
                stack.push(root);
            }

            while (!stack.isEmpty()) {
                Parent parent = stack.pop();

                boolean notFired = true;
                for (Node node : parent.getChildrenUnmodifiable()) {
                    if (node.contains(node.screenToLocal(location.getX(), location.getY()))
                            && !node.isMouseTransparent()) {
                        if (node instanceof Parent) {
                            stack.push((Parent) node);
                        } else {
                            eventTask.run(node, dragNode);
                        }
                        notFired = false;
                        break;
                    }
                }

                if (notFired) {
                    eventTask.run(parent, dragNode);
                }
            }

            if (explicit != null && dragNode != null && eventTask.getExecutions() < 1) {
                Event.fireEvent(dragNode, explicit.copyFor(this, dragNode));
                dragNodes.put(targetStage, null);
            }
        }
    }
}
