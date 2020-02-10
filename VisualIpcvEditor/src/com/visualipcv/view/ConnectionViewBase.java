package com.visualipcv.view;

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

public class ConnectionViewBase extends CubicCurve {
    private static final double BEZIER_OFFSET = 100.0;

    private DoubleProperty sourceXProperty = new SimpleDoubleProperty();
    private DoubleProperty sourceYProperty = new SimpleDoubleProperty();
    private DoubleProperty targetXProperty = new SimpleDoubleProperty();
    private DoubleProperty targetYProperty = new SimpleDoubleProperty();
    private ObjectProperty<Paint> paintProperty = new SimpleObjectProperty<>();

    public ConnectionViewBase() {
        setLayoutX(0.0);
        setLayoutY(0.0);
        setMouseTransparent(true);

        setStrokeWidth(3.0);
        setFill(Color.TRANSPARENT);
        strokeProperty().bind(paintProperty);
        setEffect(new DropShadow(5.0, Color.BLACK));

        updatePoints();

        sourceXProperty.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                updatePoints();
            }
        });

        sourceYProperty.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                updatePoints();
            }
        });

        targetXProperty.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                updatePoints();
            }
        });

        targetYProperty.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                updatePoints();
            }
        });
    }

    public DoubleProperty getSourceXProperty() {
        return sourceXProperty;
    }

    public DoubleProperty getSourceYProperty() {
        return sourceYProperty;
    }

    public DoubleProperty getTargetXProperty() {
        return targetXProperty;
    }

    public DoubleProperty getTargetYProperty() {
        return targetYProperty;
    }

    public ObjectProperty<Paint> getPaintProperty() {
        return paintProperty;
    }

    public void setSource(double x, double y) {
        sourceXProperty.set(x);
        sourceYProperty.set(y);
    }

    public Point2D getSource() {
        return new Point2D(sourceXProperty.get(), sourceYProperty.get());
    }

    public void setTarget(double x, double y) {
        targetXProperty.set(x);
        targetYProperty.set(y);
    }

    public Point2D getTarget() {
        return new Point2D(targetXProperty.get(), targetYProperty.get());
    }

    public void setPaint(Paint paint) {
        paintProperty.set(paint);
    }

    public Paint getPaint() {
        return paintProperty.get();
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
        setStartX(sourceXProperty.get());
        setStartY(sourceYProperty.get());
        setEndX(targetXProperty.get());
        setEndY(targetYProperty.get());
        setControlX1(sourceXProperty.get() + BEZIER_OFFSET);
        setControlY1(sourceYProperty.get());
        setControlX2(targetXProperty.get() - BEZIER_OFFSET);
        setControlY2(targetYProperty.get());
    }
}
