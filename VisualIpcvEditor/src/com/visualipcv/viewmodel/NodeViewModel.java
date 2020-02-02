package com.visualipcv.viewmodel;

import com.visualipcv.core.Node;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class NodeViewModel {
    private Node node;

    private DoubleProperty layoutX = new DoublePropertyBase() {
        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "layoutX";
        }

        @Override
        public void invalidated() {
            node.setLocation(layoutX.getValue(), layoutY.getValue());
        }
    };

    private DoubleProperty layoutY = new DoublePropertyBase() {
        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "layoutY";
        }

        @Override
        public void invalidated() {
            node.setLocation(layoutX.getValue(), layoutY.getValue());
        }
    };

    private StringProperty title = new SimpleStringProperty();

    public NodeViewModel(Node node) {
        this.node = node;
        title.setValue(node.getProcessor().getName());

        double x = node.getX();
        double y = node.getY();
        layoutX.setValue(x);
        layoutY.setValue(y);
    }

    public DoubleProperty getLayoutXProperty() {
        return layoutX;
    }

    public DoubleProperty getLayoutYProperty() {
        return layoutY;
    }

    public StringProperty getTitleProperty() {
        return title;
    }
}
