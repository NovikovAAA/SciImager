package org.dockfx;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class DockNode extends VBox implements EventHandler<MouseEvent> {
    private StageStyle stageStyle = StageStyle.TRANSPARENT;

    private Stage stage;

    private Node contents;
    private DockTitleBar dockTitleBar;
    private BorderPane borderPane;
    private DockPane dockPane;
    private static final PseudoClass FLOATING_PSEUDO_CLASS = PseudoClass.getPseudoClass("floating");
    private static final PseudoClass DOCKED_PSEUDO_CLASS = PseudoClass.getPseudoClass("docked");
    private static final PseudoClass MAXIMIZED_PSEUDO_CLASS = PseudoClass.getPseudoClass("maximized");

    private BooleanProperty maximizedProperty = new SimpleBooleanProperty(false) {

        @Override
        protected void invalidated() {
            DockNode.this.pseudoClassStateChanged(MAXIMIZED_PSEUDO_CLASS, get());
            if (borderPane != null) {
                borderPane.pseudoClassStateChanged(MAXIMIZED_PSEUDO_CLASS, get());
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
        this.contents = contents;

        dockTitleBar = new DockTitleBar(this);

        getChildren().addAll(dockTitleBar, contents);
        VBox.setVgrow(contents, Priority.ALWAYS);

        this.getStyleClass().add("dock-node");
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
        this.getChildren().set(this.getChildren().indexOf(this.contents), contents);
        this.contents = contents;
    }

    public void setDockTitleBar(DockTitleBar dockTitleBar) {
        if (dockTitleBar != null) {
            if (this.dockTitleBar != null) {
                this.getChildren().set(this.getChildren().indexOf(this.dockTitleBar), dockTitleBar);
            } else {
                this.getChildren().add(0, dockTitleBar);
            }
        } else {
            this.getChildren().remove(this.dockTitleBar);
        }

        this.dockTitleBar = dockTitleBar;
    }

    public final void setMaximized(boolean maximized) {
        maximizedProperty.set(maximized);
    }

    public void setFloating(boolean floating, Point2D translation) {
        if (floating && !this.isFloating()) {
            Point2D floatScene = this.localToScene(0, 0);
            Point2D floatScreen = this.localToScreen(0, 0);

            dockTitleBar.setVisible(this.isCustomTitleBar());
            dockTitleBar.setManaged(this.isCustomTitleBar());

            this.floatingProperty.set(floating);
            this.applyCss();

            if (this.isDocked()) {
                this.undock();
            }

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

            borderPane = new BorderPane();
            borderPane.getStyleClass().add("dock-node-border");
            borderPane.setCenter(this);

            Scene scene = new Scene(borderPane);

            borderPane.applyCss();
            Insets insetsDelta = borderPane.getInsets();

            double insetsWidth = insetsDelta.getLeft() + insetsDelta.getRight();
            double insetsHeight = insetsDelta.getTop() + insetsDelta.getBottom();

            stage.setX(stagePosition.getX() - insetsDelta.getLeft());
            stage.setY(stagePosition.getY() - insetsDelta.getTop());

            stage.setMinWidth(borderPane.minWidth(this.getHeight()) + insetsWidth);
            stage.setMinHeight(borderPane.minHeight(this.getWidth()) + insetsHeight);

            borderPane.setPrefSize(this.getWidth() + insetsWidth, this.getHeight() + insetsHeight);

            stage.setScene(scene);

            if (stageStyle == StageStyle.TRANSPARENT) {
                scene.setFill(null);
            }

            stage.setResizable(this.isStageResizable());
            if (this.isStageResizable()) {
                stage.addEventFilter(MouseEvent.MOUSE_PRESSED, this);
                stage.addEventFilter(MouseEvent.MOUSE_MOVED, this);
                stage.addEventFilter(MouseEvent.MOUSE_DRAGGED, this);
            }

            stage.sizeToScene();

            stage.show();
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

    public final DockTitleBar getDockTitleBar() {
        return this.dockTitleBar;
    }

    public final Stage getStage() {
        return stage;
    }

    public final BorderPane getBorderPane() {
        return borderPane;
    }

    public final Node getContents() {
        return contents;
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

    public final BooleanProperty customTitleBarProperty() {
        return customTitleBarProperty;
    }

    private BooleanProperty customTitleBarProperty = new SimpleBooleanProperty(true) {
        @Override
        public String getName() {
            return "customTitleBar";
        }
    };

    public final boolean isCustomTitleBar() {
        return customTitleBarProperty.get();
    }

    public final void setUseCustomTitleBar(boolean useCustomTitleBar) {
        if (this.isFloating()) {
            dockTitleBar.setVisible(useCustomTitleBar);
            dockTitleBar.setManaged(useCustomTitleBar);
        }
        this.customTitleBarProperty.set(useCustomTitleBar);
    }

    public final BooleanProperty floatingProperty() {
        return floatingProperty;
    }

    private BooleanProperty floatingProperty = new SimpleBooleanProperty(false) {
        @Override
        protected void invalidated() {
            DockNode.this.pseudoClassStateChanged(FLOATING_PSEUDO_CLASS, get());
            if (borderPane != null) {
                borderPane.pseudoClassStateChanged(FLOATING_PSEUDO_CLASS, get());
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
            if (get()) {
                if (dockTitleBar != null) {
                    dockTitleBar.setVisible(true);
                    dockTitleBar.setManaged(true);
                }
            }

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

    @Override
    public void handle(MouseEvent event) {
        Cursor cursor = Cursor.DEFAULT;

        if (!this.isFloating() || !this.isStageResizable()) {
            return;
        }

        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            sizeLast = new Point2D(event.getScreenX(), event.getScreenY());
        } else if (event.getEventType() == MouseEvent.MOUSE_MOVED) {
            Insets insets = borderPane.getPadding();

            sizeWest = event.getX() < insets.getLeft();
            sizeEast = event.getX() > borderPane.getWidth() - insets.getRight();
            sizeNorth = event.getY() < insets.getTop();
            sizeSouth = event.getY() > borderPane.getHeight() - insets.getBottom();

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
}
