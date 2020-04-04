package com.visualipcv.controller;

import com.visualipcv.controller.Controller;
import com.visualipcv.controller.GraphController;
import com.visualipcv.controller.binding.PropertyChangedEventListener;
import com.visualipcv.controller.binding.UIProperty;
import com.visualipcv.view.GraphView;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.CubicCurve;

public class ConnectionBaseController extends Controller<CubicCurve> {
    private static final double BEZIER_OFFSET = 100.0;

    protected UIProperty sourceXProperty = new UIProperty(0.0);
    protected UIProperty sourceYProperty = new UIProperty(0.0);
    protected UIProperty targetXProperty = new UIProperty(0.0);
    protected UIProperty targetYProperty = new UIProperty(0.0);
    protected UIProperty paintProperty = new UIProperty(0.0);

    public ConnectionBaseController() {
        super(CubicCurve.class);

        getView().setLayoutX(0.0);
        getView().setLayoutY(0.0);
        getView().setMouseTransparent(true);

        getView().setStrokeWidth(3.0);
        getView().setFill(Color.TRANSPARENT);
        getView().setEffect(new DropShadow(5.0, Color.BLACK));

        paintProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                getView().setStroke((Paint)newValue);
            }
        });

        sourceXProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                updatePoints();
            }
        });

        sourceYProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                updatePoints();
            }
        });

        targetXProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                updatePoints();
            }
        });

        targetYProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                updatePoints();
            }
        });

        initialize();
    }

    public void setSource(double x, double y) {
        sourceXProperty.setValue(x);
        sourceYProperty.setValue(y);
    }

    public Point2D getSource() {
        return new Point2D((Double)sourceXProperty.getValue(), (Double)sourceYProperty.getValue());
    }

    public void setTarget(double x, double y) {
        targetXProperty.setValue(x);
        targetYProperty.setValue(y);
    }

    public Point2D getTarget() {
        return new Point2D((Double)targetXProperty.getValue(), (Double)targetYProperty.getValue());
    }

    public void setPaint(Paint paint) {
        paintProperty.setValue(paint);
    }

    public Paint getPaint() {
        return (Paint)paintProperty.getValue();
    }

    protected Point2D localToContainerCoords(javafx.scene.Node element, double x, double y) {
        javafx.scene.Node parent = element;
        Point2D point = new Point2D(x, y);

        while(parent != null && !(parent.getParent() instanceof GraphView)) {
            point = parent.getLocalToParentTransform().transform(point);
            parent = parent.getParent();
        }

        return point;
    }

    private void updatePoints() {
        getView().setStartX((Double)sourceXProperty.getValue());
        getView().setStartY((Double)sourceYProperty.getValue());
        getView().setEndX((Double)targetXProperty.getValue());
        getView().setEndY((Double)targetYProperty.getValue());
        getView().setControlX1((Double)sourceXProperty.getValue() + BEZIER_OFFSET);
        getView().setControlY1((Double)sourceYProperty.getValue());
        getView().setControlX2((Double)targetXProperty.getValue() - BEZIER_OFFSET);
        getView().setControlY2((Double)targetYProperty.getValue());
    }
}
