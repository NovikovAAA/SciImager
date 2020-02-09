package com.visualipcv.view;

import com.visualipcv.core.Connection;
import com.visualipcv.core.Graph;
import com.visualipcv.viewmodel.ConnectionViewModel;
import com.visualipcv.viewmodel.GraphViewModel;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;

public class ConnectionView extends ConnectionViewBase {
    private ConnectionViewModel viewModel;

    private Connection connection;
    private NodeSlotView source;
    private NodeSlotView target;

    public ConnectionView(NodeSlotView source, NodeSlotView target) {
        viewModel = new ConnectionViewModel(source.getViewModel(), target.getViewModel());
        this.source = source;
        this.target = target;

        updatePoints();
        getPaintProperty().bind(source.getViewModel().getStrokeProperty());

        source.getNode().layoutXProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                updatePoints();
            }
        });

        source.getNode().layoutYProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                updatePoints();
            }
        });

        target.getNode().layoutXProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                updatePoints();
            }
        });

        target.getNode().layoutYProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                updatePoints();
            }
        });
    }

    public static Point2D localToContainerCoords(javafx.scene.Node element, double x, double y) {
        javafx.scene.Node parent = element;
        Point2D point = new Point2D(x, y);

        while(parent != null && (parent.getId() == null || !parent.getId().equals("container"))) {
            point = parent.getLocalToParentTransform().transform(point);
            parent = parent.getParent();
        }

        return point;
    }

    private void updatePoints() {
        Point2D src = localToContainerCoords(source, source.getWidth() * 0.5, source.getHeight() * 0.5);
        Point2D dst = localToContainerCoords(target, target.getWidth() * 0.5, target.getHeight() * 0.5);

        setSource(src.getX(), src.getY());
        setTarget(dst.getX(), dst.getY());
    }

    public NodeSlotView getSourceView() {
        return source;
    }

    public NodeSlotView getTargetView() {
        return target;
    }

    public ConnectionViewModel getViewModel() {
        return viewModel;
    }
}
