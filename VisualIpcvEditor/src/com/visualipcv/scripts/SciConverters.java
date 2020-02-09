package com.visualipcv.scripts;

import com.visualipcv.core.DataType;
import org.scilab.modules.types.ScilabDouble;
import org.scilab.modules.types.ScilabType;

import java.util.HashMap;
import java.util.Map;

public class SciConverters {
    private static final Map<DataType, SciConverter> converters = new HashMap<>();

    static {
        converters.put(DataType.NUMBER, new SciConverter() {
            @Override
            public ScilabType fromJavaToScilab(Object value) {
                return new ScilabDouble((Double)value);
            }
            @Override
            public Object fromScilabToJava(ScilabType value) {
                return ((ScilabDouble)value).getRealElement(0, 0);
            }
        });

        converters.put(DataType.VECTOR2, new SciConverter() {
            @Override
            public ScilabType fromJavaToScilab(Object value) {
                double[] values = (double[])value;
                return new ScilabDouble(new double[][] { values });
            }
            @Override
            public Object fromScilabToJava(ScilabType value) {
                return ((ScilabDouble)value).getRealPart()[0];
            }
        });

        converters.put(DataType.VECTOR3, new SciConverter() {
            @Override
            public ScilabType fromJavaToScilab(Object value) {
                double[] values = (double[])value;
                return new ScilabDouble(new double[][] { values });
            }
            @Override
            public Object fromScilabToJava(ScilabType value) {
                return ((ScilabDouble)value).getRealPart()[0];
            }
        });

        converters.put(DataType.VECTOR4, new SciConverter() {
            @Override
            public ScilabType fromJavaToScilab(Object value) {
                double[] values = (double[])value;
                return new ScilabDouble(new double[][] { values });
            }
            @Override
            public Object fromScilabToJava(ScilabType value) {
                return ((ScilabDouble)value).getRealPart()[0];
            }
        });

        converters.put(DataType.IMAGE, new SciConverter() {
            @Override
            public ScilabType fromJavaToScilab(Object value) {
                return new ScilabDouble((double[][])value);
            }
            @Override
            public Object fromScilabToJava(ScilabType value) {
                return ((ScilabDouble)value).getRealPart();
            }
        });
    }

    public static SciConverter getConverterForType(DataType type) {
        return converters.get(type);
    }
}
