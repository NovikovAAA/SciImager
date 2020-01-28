package com.visualipcv.scripts;

import org.scilab.modules.types.ScilabDouble;
import org.scilab.modules.types.ScilabInteger;
import org.scilab.modules.types.ScilabString;
import org.scilab.modules.types.ScilabType;

import java.util.HashMap;
import java.util.Map;

public class SciConverters {
    private static final Map<Class, SciConverter> converters = new HashMap<>();

    static {
        converters.put(Integer.class, new SciConverter() {
            @Override
            public ScilabType fromJavaToScilab(Object value) {
                return new ScilabInteger((Integer) value);
            }
            @Override
            public Object fromScilabToJava(ScilabType value) {
                return ((ScilabInteger)value).getIntElement(0, 0);
            }
        });

        converters.put(Double.class, new SciConverter() {
            @Override
            public ScilabType fromJavaToScilab(Object value) {
                return new ScilabDouble((Double)value);
            }
            @Override
            public Object fromScilabToJava(ScilabType value) {
                return ((ScilabDouble)value).getRealElement(0, 0);
            }
        });

        converters.put(Integer[].class, new SciConverter() {
            @Override
            public ScilabType fromJavaToScilab(Object value) {
                int[] values = (int[])value;
                return new ScilabInteger(new int[][] { values }, false);
            }
            @Override
            public Object fromScilabToJava(ScilabType value) {
                return ((ScilabInteger)value).getDataAsInt()[0];
            }
        });

        converters.put(Double[].class, new SciConverter() {
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

        converters.put(Integer[][].class, new SciConverter() {
            @Override
            public ScilabType fromJavaToScilab(Object value) {
                return new ScilabInteger((int[][])value, false);
            }
            @Override
            public Object fromScilabToJava(ScilabType value) {
                return ((ScilabInteger)value).getDataAsInt();
            }
        });

        converters.put(Double[][].class, new SciConverter() {
            @Override
            public ScilabType fromJavaToScilab(Object value) {
                return new ScilabDouble((double[][])value);
            }
            @Override
            public Object fromScilabToJava(ScilabType value) {
                return ((ScilabDouble)value).getRealPart();
            }
        });

        converters.put(String.class, new SciConverter() {
            @Override
            public ScilabType fromJavaToScilab(Object value) {
                return new ScilabString((String)value);
            }
            @Override
            public Object fromScilabToJava(ScilabType value) {
                return ((ScilabString)value).getData()[0][0];
            }
        });
    }

    public static SciConverter getConverterForClass(Class clazz) {
        return converters.get(clazz);
    }
}
