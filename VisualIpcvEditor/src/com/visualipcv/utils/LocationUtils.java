package com.visualipcv.utils;

import javax.swing.*;
import java.awt.*;

public class LocationUtils {
    public static Point screenToComponent(Point pos, Component component) {
        Component root = component;
        Point res = new Point(pos.x, pos.y);

        while(root != null) {
            res.x -= root.getX();
            res.y -= root.getY();
            root = root.getParent();
        }

        return res;
    }
}
