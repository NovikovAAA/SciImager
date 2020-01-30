package com.visualipcv.scripts;

import com.visualipcv.DataType;
import com.visualipcv.DataTypeLibrary;
import org.scilab.modules.types.ScilabType;

public class SciProperty {
    private Class type;
    private String name;

    public SciProperty(Class type, String name) {
        this.type = type;
        this.name = name;
    }

    public Class getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
