package com.visualipcv.controller;

import com.visualipcv.controller.Controller;
import com.visualipcv.core.DataType;
import com.visualipcv.core.InputNodeSlot;
import javafx.scene.Node;

public abstract class InputFieldFactory {
    public abstract Controller<?> create(DataType type);
}
