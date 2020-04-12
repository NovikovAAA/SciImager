package com.visualipcv.view.docking;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.sun.javafx.css.StyleManager;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import javafx.util.Duration;

public class DockPane extends StackPane implements EventHandler<DockEvent> {
    static List<DockPane> dockPanes = new ArrayList<DockPane>();

    private Node root;

    private boolean receivedEnter = false;

    private Node dockNodeDrag;
    private Node dockAreaDrag;
    private DockPos dockPosDrag;

    private Rectangle dockAreaIndicator;
    private Timeline dockAreaStrokeTimeline;
    private Popup dockIndicatorOverlay;

    private GridPane dockPosIndicator;
    private Popup dockIndicatorPopup;

    public class DockPosButton extends Button {
        private boolean dockRoot = true;
        private DockPos dockPos = DockPos.CENTER;

        public DockPosButton(boolean dockRoot, DockPos dockPos) {
            super();
            this.dockRoot = dockRoot;
            this.dockPos = dockPos;
        }

        public final void setDockRoot(boolean dockRoot) {
            this.dockRoot = dockRoot;
        }

        public final void setDockPos(DockPos dockPos) {
            this.dockPos = dockPos;
        }

        public final DockPos getDockPos() {
            return dockPos;
        }

        public final boolean isDockRoot() {
            return dockRoot;
        }
    }

    private ObservableList<DockPosButton> dockPosButtons;

    public DockPane() {
        super();
        DockPane.dockPanes.add(this);

        this.addEventHandler(DockEvent.ANY, this);
        this.addEventFilter(DockEvent.ANY, new EventHandler<DockEvent>() {

            @Override
            public void handle(DockEvent event) {

                if (event.getEventType() == DockEvent.DOCK_ENTER) {
                    DockPane.this.receivedEnter = true;
                } else if (event.getEventType() == DockEvent.DOCK_OVER) {
                    DockPane.this.dockNodeDrag = null;
                }
            }

        });

        dockIndicatorPopup = new Popup();
        dockIndicatorPopup.setAutoFix(false);

        dockIndicatorOverlay = new Popup();
        dockIndicatorOverlay.setAutoFix(false);

        StackPane dockRootPane = new StackPane();
        dockRootPane.prefWidthProperty().bind(this.widthProperty());
        dockRootPane.prefHeightProperty().bind(this.heightProperty());

        dockAreaIndicator = new Rectangle();
        dockAreaIndicator.setManaged(false);
        dockAreaIndicator.setMouseTransparent(true);

        dockAreaStrokeTimeline = new Timeline();
        dockAreaStrokeTimeline.setCycleCount(Timeline.INDEFINITE);

        KeyValue kv = new KeyValue(dockAreaIndicator.strokeDashOffsetProperty(), 12);
        KeyFrame kf = new KeyFrame(Duration.millis(500), kv);
        dockAreaStrokeTimeline.getKeyFrames().add(kf);
        dockAreaStrokeTimeline.play();

        DockPosButton dockCenter = new DockPosButton(false, DockPos.CENTER);
        dockCenter.getStyleClass().add("dock-center");

        DockPosButton dockTop = new DockPosButton(false, DockPos.TOP);
        dockTop.getStyleClass().add("dock-top");
        DockPosButton dockRight = new DockPosButton(false, DockPos.RIGHT);
        dockRight.getStyleClass().add("dock-right");
        DockPosButton dockBottom = new DockPosButton(false, DockPos.BOTTOM);
        dockBottom.getStyleClass().add("dock-bottom");
        DockPosButton dockLeft = new DockPosButton(false, DockPos.LEFT);
        dockLeft.getStyleClass().add("dock-left");

        DockPosButton dockTopRoot = new DockPosButton(true, DockPos.TOP);
        StackPane.setAlignment(dockTopRoot, Pos.TOP_CENTER);
        dockTopRoot.getStyleClass().add("dock-top-root");

        DockPosButton dockRightRoot = new DockPosButton(true, DockPos.RIGHT);
        StackPane.setAlignment(dockRightRoot, Pos.CENTER_RIGHT);
        dockRightRoot.getStyleClass().add("dock-right-root");

        DockPosButton dockBottomRoot = new DockPosButton(true, DockPos.BOTTOM);
        StackPane.setAlignment(dockBottomRoot, Pos.BOTTOM_CENTER);
        dockBottomRoot.getStyleClass().add("dock-bottom-root");

        DockPosButton dockLeftRoot = new DockPosButton(true, DockPos.LEFT);
        StackPane.setAlignment(dockLeftRoot, Pos.CENTER_LEFT);
        dockLeftRoot.getStyleClass().add("dock-left-root");

        dockPosButtons = FXCollections.observableArrayList(dockCenter, dockTop, dockRight, dockBottom, dockLeft,
                dockTopRoot, dockRightRoot, dockBottomRoot, dockLeftRoot);

        dockPosIndicator = new GridPane();
        dockPosIndicator.add(dockCenter, 1, 1);
        dockPosIndicator.add(dockTop, 1, 0);
        dockPosIndicator.add(dockRight, 2, 1);
        dockPosIndicator.add(dockBottom, 1, 2);
        dockPosIndicator.add(dockLeft, 0, 1);

        dockRootPane.getChildren().addAll(dockAreaIndicator, dockTopRoot, dockRightRoot, dockBottomRoot,
                dockLeftRoot);

        dockIndicatorOverlay.getContent().add(dockRootPane);
        dockIndicatorPopup.getContent().addAll(dockPosIndicator);

        this.getStyleClass().add("dock-pane");
        dockRootPane.getStyleClass().add("dock-root-pane");
        dockPosIndicator.getStyleClass().add("dock-pos-indicator");
        dockAreaIndicator.getStyleClass().add("dock-area-indicator");
    }

    public final Timeline getDockAreaStrokeTimeline() {
        return dockAreaStrokeTimeline;
    }

    public boolean isLastDockNode(DockNode node) {
        Stack<Node> nodes = new Stack<>();
        int dockNodeCount = 0;
        nodes.push(root);

        while(!nodes.isEmpty()) {
            Node nextNode = nodes.pop();

            if(nextNode instanceof SplitPane) {
                for(Node child : ((SplitPane)nextNode).getItems())
                    nodes.push(child);
            } else if(nextNode instanceof DockNode) {
                dockNodeCount++;
            }
        }

        return dockNodeCount == 1;
    }

    public final static String getDefaultUserAgentStyleheet() {
        return DockPane.class.getResource("default.css").toExternalForm();
    }

    public final static void initializeDefaultUserAgentStylesheet() {
        StyleManager.getInstance()
                .addUserAgentStylesheet(DockPane.class.getResource("default.css").toExternalForm());
    }

    private ObservableMap<Node, DockNodeEventHandler> dockNodeEventFilters =
            FXCollections.observableHashMap();

    private class DockNodeEventHandler implements EventHandler<DockEvent> {
        private Node node = null;

        public DockNodeEventHandler(Node node) {
            this.node = node;
        }

        @Override
        public void handle(DockEvent event) {
            DockPane.this.dockNodeDrag = node;
        }
    }

    public void dock(Node node, DockPos dockPos, Node sibling) {
        DockNodeEventHandler dockNodeEventHandler = new DockNodeEventHandler(node);
        dockNodeEventFilters.put(node, dockNodeEventHandler);
        node.addEventFilter(DockEvent.DOCK_OVER, dockNodeEventHandler);

        if(dockPos == DockPos.CENTER && sibling != null) {
            if(!(sibling instanceof DockNode))
                return;

            DockNode targetDockNode = (DockNode)sibling;
            DockNode dockNode = (DockNode)node;
            targetDockNode.merge(dockNode);
        }

        SplitPane split = (SplitPane) root;
        if (split == null) {
            split = new SplitPane();
            split.getItems().add(node);
            root = split;
            this.getChildren().add(root);
            return;
        }

        if (sibling != null && sibling != root) {
            Stack<Parent> stack = new Stack<>();
            stack.push((Parent) root);
            while (!stack.isEmpty()) {
                Parent parent = stack.pop();

                ObservableList<Node> children = parent.getChildrenUnmodifiable();

                if (parent instanceof SplitPane) {
                    SplitPane splitPane = (SplitPane) parent;
                    children = splitPane.getItems();
                }

                for (int i = 0; i < children.size(); i++) {
                    if (children.get(i) == sibling) {
                        split = (SplitPane) parent;
                    } else if (children.get(i) instanceof Parent) {
                        stack.push((Parent) children.get(i));
                    }
                }
            }
        }

        Orientation requestedOrientation = (dockPos == DockPos.LEFT || dockPos == DockPos.RIGHT)
                ? Orientation.HORIZONTAL : Orientation.VERTICAL;

        if (split.getOrientation() != requestedOrientation) {
            if (split.getItems().size() > 1) {
                SplitPane splitPane = new SplitPane();
                if (split == root && sibling == root) {
                    this.getChildren().set(this.getChildren().indexOf(root), splitPane);
                    splitPane.getItems().add(split);
                    root = splitPane;
                } else {
                    split.getItems().set(split.getItems().indexOf(sibling), splitPane);
                    splitPane.getItems().add(sibling);
                }

                split = splitPane;
            }
            split.setOrientation(requestedOrientation);
        }

        ObservableList<Node> splitItems = split.getItems();

        double magnitude = 0;

        if (splitItems.size() > 0) {
            if (split.getOrientation() == Orientation.HORIZONTAL) {
                for (Node splitItem : splitItems) {
                    magnitude += splitItem.prefWidth(0);
                }
            } else {
                for (Node splitItem : splitItems) {
                    magnitude += splitItem.prefHeight(0);
                }
            }
        }

        if (dockPos == DockPos.LEFT || dockPos == DockPos.TOP) {
            int relativeIndex = 0;
            if (sibling != null && sibling != root) {
                relativeIndex = splitItems.indexOf(sibling);
            }

            splitItems.add(relativeIndex, node);

            if (splitItems.size() > 1) {
                if (split.getOrientation() == Orientation.HORIZONTAL) {
                    split.setDividerPosition(relativeIndex,
                            node.prefWidth(0) / (magnitude + node.prefWidth(0)));
                } else {
                    split.setDividerPosition(relativeIndex,
                            node.prefHeight(0) / (magnitude + node.prefHeight(0)));
                }
            }
        } else if (dockPos == DockPos.RIGHT || dockPos == DockPos.BOTTOM) {
            int relativeIndex = splitItems.size();
            if (sibling != null && sibling != root) {
                relativeIndex = splitItems.indexOf(sibling) + 1;
            }

            splitItems.add(relativeIndex, node);
            if (splitItems.size() > 1) {
                if (split.getOrientation() == Orientation.HORIZONTAL) {
                    split.setDividerPosition(relativeIndex - 1,
                            1 - node.prefWidth(0) / (magnitude + node.prefWidth(0)));
                } else {
                    split.setDividerPosition(relativeIndex - 1,
                            1 - node.prefHeight(0) / (magnitude + node.prefHeight(0)));
                }
            }
        }
    }

    public void dock(Node node, DockPos dockPos) {
        dock(node, dockPos, root);
    }

    public void undock(DockNode node) {
        DockNodeEventHandler dockNodeEventHandler = dockNodeEventFilters.get(node);
        node.removeEventFilter(DockEvent.DOCK_OVER, dockNodeEventHandler);
        dockNodeEventFilters.remove(node);

        Stack<Parent> findStack = new Stack<>();
        findStack.push((Parent) root);
        while (!findStack.isEmpty()) {
            Parent parent = findStack.pop();

            ObservableList<Node> children = parent.getChildrenUnmodifiable();

            if (parent instanceof SplitPane) {
                SplitPane split = (SplitPane) parent;
                children = split.getItems();
            }

            for (int i = 0; i < children.size(); i++) {
                if (children.get(i) == node) {
                    children.remove(i);

                    Stack<Parent> clearStack = new Stack<Parent>();
                    clearStack.push((Parent) root);
                    while (!clearStack.isEmpty()) {
                        parent = clearStack.pop();

                        children = parent.getChildrenUnmodifiable();

                        if (parent instanceof SplitPane) {
                            SplitPane split = (SplitPane) parent;
                            children = split.getItems();
                        }

                        for (i = 0; i < children.size(); i++) {
                            if (children.get(i) instanceof SplitPane) {
                                SplitPane split = (SplitPane) children.get(i);
                                if (split.getItems().size() < 1) {
                                    children.remove(i);
                                    continue;
                                } else {
                                    clearStack.push(split);
                                }
                            }

                        }
                    }

                    return;
                } else if (children.get(i) instanceof Parent) {
                    findStack.push((Parent) children.get(i));
                }
            }
        }
    }

    @Override
    public void handle(DockEvent event) {
        if (event.getEventType() == DockEvent.DOCK_ENTER) {
            if (!dockIndicatorOverlay.isShowing()) {
                Point2D topLeft = DockPane.this.localToScreen(0, 0);
                dockIndicatorOverlay.show(DockPane.this, topLeft.getX(), topLeft.getY());
            }
        } else if (event.getEventType() == DockEvent.DOCK_OVER) {
            this.receivedEnter = false;

            dockPosDrag = null;
            dockAreaDrag = dockNodeDrag;

            for (DockPosButton dockIndicatorButton : dockPosButtons) {
                if (dockIndicatorButton
                        .contains(dockIndicatorButton.screenToLocal(event.getScreenX(), event.getScreenY()))) {
                    dockPosDrag = dockIndicatorButton.getDockPos();
                    if (dockIndicatorButton.isDockRoot()) {
                        dockAreaDrag = root;
                    }
                    dockIndicatorButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("focused"), true);
                    break;
                } else {
                    dockIndicatorButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("focused"), false);
                }
            }

            if (dockPosDrag != null) {
                Point2D originToScene = dockAreaDrag.localToScene(0, 0).subtract(this.localToScene(0, 0));

                dockAreaIndicator.setVisible(true);
                dockAreaIndicator.relocate(originToScene.getX(), originToScene.getY());
                if (dockPosDrag == DockPos.RIGHT) {
                    dockAreaIndicator.setTranslateX(dockAreaDrag.getLayoutBounds().getWidth() / 2);
                } else {
                    dockAreaIndicator.setTranslateX(0);
                }

                if (dockPosDrag == DockPos.BOTTOM) {
                    dockAreaIndicator.setTranslateY(dockAreaDrag.getLayoutBounds().getHeight() / 2);
                } else {
                    dockAreaIndicator.setTranslateY(0);
                }

                if (dockPosDrag == DockPos.LEFT || dockPosDrag == DockPos.RIGHT) {
                    dockAreaIndicator.setWidth(dockAreaDrag.getLayoutBounds().getWidth() / 2);
                } else {
                    dockAreaIndicator.setWidth(dockAreaDrag.getLayoutBounds().getWidth());
                }
                if (dockPosDrag == DockPos.TOP || dockPosDrag == DockPos.BOTTOM) {
                    dockAreaIndicator.setHeight(dockAreaDrag.getLayoutBounds().getHeight() / 2);
                } else {
                    dockAreaIndicator.setHeight(dockAreaDrag.getLayoutBounds().getHeight());
                }
            } else {
                dockAreaIndicator.setVisible(false);
            }

            if (dockNodeDrag != null) {
                Point2D originToScreen = dockNodeDrag.localToScreen(0, 0);

                double posX = originToScreen.getX() + dockNodeDrag.getLayoutBounds().getWidth() / 2
                        - dockPosIndicator.getWidth() / 2;
                double posY = originToScreen.getY() + dockNodeDrag.getLayoutBounds().getHeight() / 2
                        - dockPosIndicator.getHeight() / 2;

                if (!dockIndicatorPopup.isShowing()) {
                    dockIndicatorPopup.show(DockPane.this, posX, posY);
                } else {
                    dockIndicatorPopup.setX(posX);
                    dockIndicatorPopup.setY(posY);
                }

                dockPosIndicator.setVisible(true);
            } else {
                dockPosIndicator.setVisible(false);
            }
        }

        if (event.getEventType() == DockEvent.DOCK_RELEASED && event.getContents() != null) {
            if (dockPosDrag != null && dockIndicatorOverlay.isShowing()) {
                DockNode dockNode = (DockNode) event.getContents();
                dockNode.dock(this, dockPosDrag, dockAreaDrag);
            }
        }

        if ((event.getEventType() == DockEvent.DOCK_EXIT && !this.receivedEnter)
                || event.getEventType() == DockEvent.DOCK_RELEASED) {
            if (dockIndicatorPopup.isShowing()) {
                dockIndicatorOverlay.hide();
                dockIndicatorPopup.hide();
            }
        }
    }
}
