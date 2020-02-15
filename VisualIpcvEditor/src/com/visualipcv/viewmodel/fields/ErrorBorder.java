package com.visualipcv.viewmodel.fields;

import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class ErrorBorder {
    public static Border create() {
        return new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(0.0), new BorderWidths(3.0)));
    }
}
