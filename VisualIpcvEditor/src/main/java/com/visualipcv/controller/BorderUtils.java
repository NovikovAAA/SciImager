package com.visualipcv.controller;

import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class BorderUtils {
    public static Border createErrorBorder() {
        return new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(0.0), new BorderWidths(3.0)));
    }

    public static Border createHighlightBorder() {
        return new Border(new BorderStroke(Color.ORANGE, BorderStrokeStyle.SOLID, new CornerRadii(10.0), new BorderWidths(3.0)));
    }
}
