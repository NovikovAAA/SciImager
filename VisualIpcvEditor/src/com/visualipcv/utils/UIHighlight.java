package com.visualipcv.utils;

import javafx.scene.control.Control;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;

import java.util.HashMap;
import java.util.Map;

public class UIHighlight {
    private static Map<Control, Paint> oldColors = new HashMap<>();

    public static void highlight(Control control, Paint paint) {
        if(oldColors.containsKey(control))
            return;

        Border border = control.getBorder();

        if(border == null) {
            border = new Border(new BorderStroke(paint, BorderStrokeStyle.SOLID, new CornerRadii(0.0), new BorderWidths(2.0)));
            oldColors.put(control, null);
        } else {
            BorderStroke stroke = border.getStrokes().get(0);
            oldColors.put(control, stroke.getBottomStroke());
        }

        BorderStroke stroke = border.getStrokes().get(0);
        Border newBorder = new Border(new BorderStroke(paint, stroke.getBottomStyle(), stroke.getRadii(), stroke.getWidths()));
        control.setBorder(newBorder);
    }

    public static void removeHighlight(Control control) {
        if(!oldColors.containsKey(control))
            return;

        if(oldColors.get(control) == null) {
            control.setBorder(null);
            oldColors.remove(control);
            return;
        }

        Border border = control.getBorder();
        BorderStroke stroke = border.getStrokes().get(0);

        Border newBorder = new Border(new BorderStroke(oldColors.get(control), stroke.getBottomStyle(), stroke.getRadii(), stroke.getWidths()));
        control.setBorder(newBorder);

        oldColors.remove(control);
    }
}
