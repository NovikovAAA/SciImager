package com.visualipcv.view;

import com.visualipcv.core.DataType;
import com.visualipcv.core.InputNodeSlot;
import com.visualipcv.core.NodeSlot;
import javafx.scene.Node;

public abstract class InputFieldFactory {
    public abstract Node create(InputFieldView field, InputNodeSlot nodeSlot);
}
