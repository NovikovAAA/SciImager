package com.visualipcv.editor;

import org.dockfx.DockPos;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EditorWindow {
    String path();
    String name();
    DockPos dockPos();
}
