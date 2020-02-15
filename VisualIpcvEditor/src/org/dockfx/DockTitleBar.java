package org.dockfx;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.stage.Window;

public class DockTitleBar extends HBox implements EventHandler<MouseEvent> {

  private DockNode dockNode;
  private Label label;
  private Button closeButton, stateButton;

  public DockTitleBar(DockNode dockNode) {
    this.dockNode = dockNode;

    label = new Label("Dock Title Bar");
    label.textProperty().bind(dockNode.titleProperty());
    label.graphicProperty().bind(dockNode.graphicProperty());

    stateButton = new Button();
    stateButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        if (dockNode.isFloating()) {
          dockNode.setMaximized(!dockNode.isMaximized());
        } else {
          dockNode.setFloating(true);
        }
      }
    });

    closeButton = new Button();
    closeButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        dockNode.close();
      }
    });
    closeButton.visibleProperty().bind(dockNode.closableProperty());

    Pane fillPane = new Pane();
    HBox.setHgrow(fillPane, Priority.ALWAYS);

    getChildren().addAll(label, fillPane, stateButton, closeButton);

    this.addEventHandler(MouseEvent.MOUSE_PRESSED, this);
    this.addEventHandler(MouseEvent.DRAG_DETECTED, this);
    this.addEventHandler(MouseEvent.MOUSE_DRAGGED, this);
    this.addEventHandler(MouseEvent.MOUSE_RELEASED, this);

    label.getStyleClass().add("dock-title-label");
    closeButton.getStyleClass().add("dock-close-button");
    stateButton.getStyleClass().add("dock-state-button");
    this.getStyleClass().add("dock-title-bar");
  }

  public final boolean isDragging() {
    return dragging;
  }

  public final Label getLabel() {
    return label;
  }

  public final Button getCloseButton() {
    return closeButton;
  }

  public final Button getStateButton() {
    return stateButton;
  }

  public final DockNode getDockNode() {
    return dockNode;
  }

  private Point2D dragStart;
  private boolean dragging = false;
  private HashMap<Window, Node> dragNodes = new HashMap<Window, Node>();

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

  private void pickEventTarget(Point2D location, EventTask eventTask, Event explicit) {
    List<DockPane> dockPanes = DockPane.dockPanes;

    for (DockPane dockPane : dockPanes) {
      Window window = dockPane.getScene().getWindow();
      if (!(window instanceof Stage)) continue;
      Stage targetStage = (Stage) window;

      if (targetStage == this.dockNode.getStage())
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

  @Override
  public void handle(MouseEvent event) {
    if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
      if (dockNode.isFloating() && event.getClickCount() == 2
          && event.getButton() == MouseButton.PRIMARY) {
        dockNode.setMaximized(!dockNode.isMaximized());
      } else {
        dragStart = new Point2D(event.getX(), event.getY());
      }
    } else if (event.getEventType() == MouseEvent.DRAG_DETECTED) {
      if (!dockNode.isFloating()) {
        if (!dockNode.isCustomTitleBar() && dockNode.isDecorated()) {
          dockNode.setFloating(true, new Point2D(0, DockTitleBar.this.getHeight()));
        } else {
          dockNode.setFloating(true);
        }

        DockPane dockPane = this.getDockNode().getDockPane();
        if (dockPane != null) {
          dockPane.addEventFilter(MouseEvent.MOUSE_DRAGGED, this);
          dockPane.addEventFilter(MouseEvent.MOUSE_RELEASED, this);
        }
      } else if (dockNode.isMaximized()) {
        double ratioX = event.getX() / this.getDockNode().getWidth();
        double ratioY = event.getY() / this.getDockNode().getHeight();

        dockNode.setMaximized(false);

        dragStart = new Point2D(ratioX * dockNode.getWidth(), ratioY * dockNode.getHeight());
      }
      dragging = true;
      event.consume();
    } else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
      if (dockNode.isFloating() && event.getClickCount() == 2
          && event.getButton() == MouseButton.PRIMARY) {
        event.setDragDetect(false);
        event.consume();
        return;
      }

      if (!dragging)
        return;

      Stage stage = dockNode.getStage();
      Insets insetsDelta = this.getDockNode().getBorderPane().getInsets();

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
            Event.fireEvent(node, dockEnterEvent.copyFor(DockTitleBar.this, node));

            if (dragNode != null) {
              Event.fireEvent(dragNode, dockExitEvent.copyFor(DockTitleBar.this, dragNode));
            }

            dragNodes.put(node.getScene().getWindow(), node);
          }
          Event.fireEvent(node, dockOverEvent.copyFor(DockTitleBar.this, node));
        }
      };

      this.pickEventTarget(new Point2D(event.getScreenX(), event.getScreenY()), eventTask,
          dockExitEvent);
    } else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
      dragging = false;

      DockEvent dockReleasedEvent =
          new DockEvent(this, DockEvent.NULL_SOURCE_TARGET, DockEvent.DOCK_RELEASED, event.getX(),
              event.getY(), event.getScreenX(), event.getScreenY(), null, this.getDockNode());

      EventTask eventTask = new EventTask() {
        @Override
        public void run(Node node, Node dragNode) {
          executions++;
          if (dragNode != node) {
            Event.fireEvent(node, dockReleasedEvent.copyFor(DockTitleBar.this, node));
          }
          Event.fireEvent(node, dockReleasedEvent.copyFor(DockTitleBar.this, node));
        }
      };

      this.pickEventTarget(new Point2D(event.getScreenX(), event.getScreenY()), eventTask, null);

      dragNodes.clear();

      DockPane dockPane = this.getDockNode().getDockPane();
      if (dockPane != null) {
        dockPane.removeEventFilter(MouseEvent.MOUSE_DRAGGED, this);
        dockPane.removeEventFilter(MouseEvent.MOUSE_RELEASED, this);
      }
    }
  }
}
