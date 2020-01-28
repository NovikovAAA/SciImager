package com.visualipcv.scripts;

import org.scilab.modules.types.ScilabType;

public abstract class SciConverter {
    public abstract ScilabType fromJavaToScilab(Object value);
    public abstract Object fromScilabToJava(ScilabType value);
}
