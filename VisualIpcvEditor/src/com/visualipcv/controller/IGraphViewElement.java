package com.visualipcv.controller;

import com.visualipcv.view.GraphView;

public interface IGraphViewElement {
    GraphViewController getController();
    GraphView getGraphView();
}
