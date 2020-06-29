package com.visualipcv.scripts;

import org.scilab.modules.types.ScilabType;

public abstract class SciConverter {
    public abstract ScilabType fromJavaToScilab(String name, Object value);
    public abstract Object fromScilabToJava(String name, ScilabType value);
}
