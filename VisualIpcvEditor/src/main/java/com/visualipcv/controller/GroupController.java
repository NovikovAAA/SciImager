package com.visualipcv.controller;

import com.visualipcv.controller.binding.Binder;
import com.visualipcv.controller.binding.PropertyChangedEventListener;
import com.visualipcv.controller.binding.UIProperty;
import com.visualipcv.core.GraphElement;
import com.visualipcv.core.Group;
import com.visualipcv.editor.Editor;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.awt.*;

public class GroupController extends GraphElementController<AnchorPane> {
    private final UIProperty widthProperty = new UIProperty();
    private final UIProperty heightProperty = new UIProperty();

    private ContextMenu contextMenu;

    public GroupController(GraphController graphController) {
        super(graphController, AnchorPane.class, "GroupView.fxml");

        widthProperty.setBinder(new Binder() {
            @Override
            public Object update(Object context) {
                return ((Group)context).getWidth();
            }
        });

        heightProperty.setBinder(new Binder() {
            @Override
            public Object update(Object context) {
                return ((Group)context).getHeight();
            }
        });

        widthProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                getView().setPrefWidth((Double)newValue);
            }
        });

        heightProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                getView().setPrefHeight((Double)newValue);
            }
        });

        initialize();

        getView().addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton() == MouseButton.SECONDARY) {
                    contextMenu.show(getView(), event.getScreenX(), event.getScreenY());
                    event.consume();
                }
            }
        });

        getView().addEventHandler(MouseEvent.ANY, this::handleResize);
    }

    private void createContextMenu() {
        contextMenu = new ContextMenu();

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                getGraphController().removeSelected();
            }
        });

        contextMenu.getItems().add(deleteItem);

        if(((GraphElement)getContext()).getDescription().isEmpty()) {
            MenuItem descriptionItem = new MenuItem("Add description");
            descriptionItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    ((GraphElement)getContext()).setDescription("Just a group");
                    invalidate();
                }
            });

            contextMenu.getItems().add(descriptionItem);
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        createContextMenu();
    }

    private Point2D initialMin;
    private Point2D initialMax;
    private boolean sizeWest = false, sizeEast = false, sizeNorth = false, sizeSouth = false;

    public boolean isMouseResizeZone() {
        return sizeWest || sizeEast || sizeNorth || sizeSouth;
    }

    @Override
    public void onMouseDragged(MouseEvent event) {
        if(isMouseResizeZone())
            return;
        super.onMouseDragged(event);
    }

    private void handleResize(MouseEvent event) {
        Cursor cursor = Cursor.DEFAULT;

        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            initialMin = new Point2D(getView().getLayoutX(), getView().getLayoutY());
            initialMax = new Point2D(getView().getPrefWidth(), getView().getPrefHeight()).add(initialMin);
        } else if(event.getEventType() == MouseEvent.MOUSE_EXITED) {
            Editor.getPrimaryStage().getScene().setCursor(Cursor.DEFAULT);
        } else if (event.getEventType() == MouseEvent.MOUSE_MOVED) {
            Insets insets = new Insets(5);

            sizeWest = event.getX() < insets.getLeft();
            sizeEast = event.getX() > getView().getWidth() - insets.getRight();
            sizeNorth = event.getY() < insets.getTop();
            sizeSouth = event.getY() > getView().getHeight() - insets.getBottom();

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

            Editor.getPrimaryStage().getScene().setCursor(cursor);
        } else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED && this.isMouseResizeZone()) {
            Point2D sizeCurrent = new Point2D(event.getX(), event.getY());
            sizeCurrent = getView().localToParent(sizeCurrent);

            double newX = getView().getLayoutX(), newY = getView().getLayoutY(),
                    newWidth = getView().getWidth(),
                    newHeight = getView().getHeight();

            if (sizeNorth) {
                newY = sizeCurrent.getY();
                newHeight = initialMax.getY() - newY;
            } else if (sizeSouth) {
                newHeight = sizeCurrent.getY() - initialMin.getY();
            }

            if (sizeWest) {
                newX = sizeCurrent.getX();
                newWidth = initialMax.getX() - newX;
            } else if (sizeEast) {
                newWidth = sizeCurrent.getX() - initialMin.getX();
            }

            Group group = getContext();

            if (newWidth >= getView().getMinWidth()) {
                group.setLocation(newX, group.getY());
                group.setWidth(newWidth);
            }

            if (newHeight >= getView().getMinHeight()) {
                group.setLocation(group.getX(), newY);
                group.setHeight(newHeight);
            }

            invalidate();

            if (sizeNorth || sizeSouth || sizeWest || sizeEast) {
                event.consume();
            }
        }
    }
}
