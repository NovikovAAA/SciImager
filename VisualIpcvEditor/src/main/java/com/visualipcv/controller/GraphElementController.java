package com.visualipcv.controller;

import com.visualipcv.controller.binding.PropertyChangedEventListener;
import com.visualipcv.controller.binding.UIProperty;
import com.visualipcv.core.GraphElement;
import com.visualipcv.core.Group;
import com.visualipcv.procs.properties.StringProperty;
import com.visualipcv.view.EditableLabel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

public class GraphElementController <T extends Region> extends Controller<T> {
    private GraphController graphController;

    private double previousMouseX;
    private double previousMouseY;

    @FXML
    private EditableLabel title;
    @FXML
    private AnchorPane wrapper;
    @FXML
    private EditableLabel description;

    private final UIProperty selectedProperty = new UIProperty(false);
    private final UIProperty titleProperty = new UIProperty();
    private final UIProperty descriptionProperty = new UIProperty();
    private final UIProperty xOffsetProperty = new UIProperty();
    private final UIProperty yOffsetProperty = new UIProperty();

    public GraphElementController(GraphController graphController, Class<T> viewClass, String fxmlPath) {
        super(viewClass, fxmlPath);
        this.graphController = graphController;

        titleProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                title.setText((String)newValue);
            }
        });

        descriptionProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                description.setText((String)newValue);
                description.setVisible(!description.getText().isEmpty());
            }
        });

        selectedProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                if((Boolean)newValue) {
                    wrapper.setBorder(BorderUtils.createHighlightBorder());
                    AnchorPane.setBottomAnchor(wrapper, 0.0);
                    AnchorPane.setTopAnchor(wrapper, 0.0);
                    AnchorPane.setLeftAnchor(wrapper, 0.0);
                    AnchorPane.setRightAnchor(wrapper, 0.0);
                }
                else {
                    wrapper.setBorder(null);
                    AnchorPane.setBottomAnchor(wrapper, 3.0);
                    AnchorPane.setTopAnchor(wrapper, 3.0);
                    AnchorPane.setLeftAnchor(wrapper, 3.0);
                    AnchorPane.setRightAnchor(wrapper, 3.0);
                }
            }
        });

        xOffsetProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                getView().setLayoutX((Double)newValue);
            }
        });

        yOffsetProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                getView().setLayoutY((Double)newValue);
            }
        });

        title.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ((GraphElement)getContext()).setName(newValue);
            }
        });

        description.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ((GraphElement)getContext()).setDescription(newValue);
                invalidate();
            }
        });

        titleProperty.setBinder((Object node) -> {
            return ((GraphElement)node).getName();
        });

        descriptionProperty.setBinder((Object node) -> {
            return ((GraphElement)node).getDescription();
        });

        xOffsetProperty.setBinder((Object node) -> {
            return ((GraphElement)node).getX();
        });

        yOffsetProperty.setBinder((Object node) -> {
            return ((GraphElement)node).getY();
        });
    }

    public void setSelected(boolean selected) {
        this.selectedProperty.setValue(selected);
    }

    public boolean isSelected() {
        return (Boolean)selectedProperty.getValue();
    }

    public void moveTo(double x, double y) {
        ((GraphElement)getContext()).setLocation(x, y);
        poll(xOffsetProperty);
        poll(yOffsetProperty);
    }

    @FXML
    public void onMousePressed(MouseEvent event) {
        previousMouseX = event.getScreenX();
        previousMouseY = event.getScreenY();

        graphController.select(this, event.isControlDown());
        event.consume();
    }

    @FXML
    public void onMouseDragged(MouseEvent event) {
        double deltaX = event.getScreenX() - previousMouseX;
        double deltaY = event.getScreenY() - previousMouseY;
        previousMouseX = event.getScreenX();
        previousMouseY = event.getScreenY();

        if(event.getButton() == MouseButton.PRIMARY) {
            getGraphController().moveSelected(deltaX, deltaY);
            event.consume();
        }
    }

    @FXML
    public void onKeyPressed(KeyEvent event) {
        if(event.getCode() == KeyCode.DELETE) {
            getGraphController().removeSelected();
            event.consume();
        }
    }

    public GraphController getGraphController() {
        return graphController;
    }

    public UIProperty titleProperty() {
        return titleProperty;
    }

    public UIProperty xOffsetProperty() {
        return xOffsetProperty;
    }

    public UIProperty yOffsetProperty() {
        return yOffsetProperty;
    }

    public EditableLabel getTitle() {
        return title;
    }
}
