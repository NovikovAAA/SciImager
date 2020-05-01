package com.visualipcv.view;

import javafx.collections.ListChangeListener;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Region;

public class AdaptiveSplitPane extends SplitPane {
    public AdaptiveSplitPane() {
        super();

        getItems().addListener(new ListChangeListener<Node>() {
            @Override
            public void onChanged(Change<? extends Node> c) {
                growSize();
            }
        });
    }

    private void growSize() {
        double width = 0.0;
        double height = 0.0;

        for(Node node : getItems()) {
            Region region = (Region)node;

            if(getOrientation() == Orientation.HORIZONTAL) {
                width += region.getPrefWidth();
                height = Math.max(region.getPrefHeight(), height);
            } else {
                height += region.getPrefHeight();
                width = Math.max(region.getPrefWidth(), width);
            }
        }

        setPrefWidth(width);
        setPrefHeight(height);
    }
}
