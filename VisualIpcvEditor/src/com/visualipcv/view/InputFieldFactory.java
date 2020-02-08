package com.visualipcv.view;

import com.visualipcv.core.DataType;
import javafx.scene.Node;

public abstract class InputFieldFactory {
    public abstract Node create(InputFieldView field, DataType type);
}
