package com.visualipcv.scripts;

import com.visualipcv.core.DataType;
import com.visualipcv.core.DataTypes;
import com.visualipcv.core.OpenCvUtils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.scilab.modules.external_objects_java.ScilabJavaArray;
import org.scilab.modules.types.ScilabDouble;
import org.scilab.modules.types.ScilabInteger;
import org.scilab.modules.types.ScilabMList;
import org.scilab.modules.types.ScilabString;
import org.scilab.modules.types.ScilabType;
import org.scilab.modules.types.ScilabTypeEnum;

import java.util.HashMap;
import java.util.Map;

public class SciConverters {
    private static final Map<DataType, SciConverter> converters = new HashMap<>();

    public static void load() {
        converters.put(DataTypes.DOUBLE, new SciConverter() {
            @Override
            public ScilabType fromJavaToScilab(Object value) {
                return new ScilabDouble((Double)value);
            }
            @Override
            public Object fromScilabToJava(ScilabType value) {
                if(value == null)
                    return 0.0;

                return ((ScilabDouble)value).getRealElement(0, 0);
            }
        });

        converters.put(DataTypes.INTEGER, new SciConverter() {
            @Override
            public ScilabType fromJavaToScilab(Object value) {
                return new ScilabInteger((Integer)value);
            }

            @Override
            public Object fromScilabToJava(ScilabType value) {
                if(value == null)
                    return 0;

                return ((ScilabInteger)value).getIntElement(0, 0);
            }
        });

        converters.put(DataTypes.VECTOR2, new SciConverter() {
            @Override
            public ScilabType fromJavaToScilab(Object value) {
                Double[] values = (Double[])value;
                double[][] fuckingPrimitivesForFuckingJava = new double[1][2];

                for(int i = 0; i < values.length; i++)
                    fuckingPrimitivesForFuckingJava[0][i] = values[i];

                return new ScilabDouble(fuckingPrimitivesForFuckingJava);
            }
            @Override
            public Object fromScilabToJava(ScilabType value) {
                if(value == null)
                    return new Double[2];

                double[] res = ((ScilabDouble)value).getRealPart()[0];
                Double[] fuckingJava = new Double[2];

                for(int i = 0; i < fuckingJava.length; i++) {
                    fuckingJava[i] = res[i];
                }

                return fuckingJava;
            }
        });

        converters.put(DataTypes.VECTOR3, new SciConverter() {
            @Override
            public ScilabType fromJavaToScilab(Object value) {
                Double[] values = (Double[])value;
                double[][] fuckingPrimitivesForFuckingJava = new double[1][3];

                for(int i = 0; i < values.length; i++)
                    fuckingPrimitivesForFuckingJava[0][i] = values[i];

                return new ScilabDouble(fuckingPrimitivesForFuckingJava);
            }
            @Override
            public Object fromScilabToJava(ScilabType value) {
                if(value == null)
                    return new Double[3];

                double[] res = ((ScilabDouble)value).getRealPart()[0];
                Double[] fuckingJava = new Double[3];

                for(int i = 0; i < fuckingJava.length; i++) {
                    fuckingJava[i] = res[i];
                }

                return fuckingJava;
            }
        });

        converters.put(DataTypes.VECTOR4, new SciConverter() {
            @Override
            public ScilabType fromJavaToScilab(Object value) {
                Double[] values = (Double[])value;
                double[][] fuckingPrimitivesForFuckingJava = new double[1][4];

                for(int i = 0; i < values.length; i++)
                    fuckingPrimitivesForFuckingJava[0][i] = values[i];

                return new ScilabDouble(fuckingPrimitivesForFuckingJava);
            }
            @Override
            public Object fromScilabToJava(ScilabType value) {
                if(value == null)
                    return new Double[4];

                double[] res = ((ScilabDouble)value).getRealPart()[0];
                Double[] fuckingJava = new Double[4];

                for(int i = 0; i < fuckingJava.length; i++) {
                    fuckingJava[i] = res[i];
                }

                return fuckingJava;
            }
        });

        converters.put(DataTypes.STRING, new SciConverter() {
            @Override
            public ScilabType fromJavaToScilab(Object value) {
                return new ScilabString((String)value);
            }

            @Override
            public Object fromScilabToJava(ScilabType value) {
                if(value == null)
                    return "";

                return ((ScilabString)value).getData()[0][0];
            }
        });

        converters.put(DataTypes.IMAGE, new SciConverter() {
            @Override
            public ScilabType fromJavaToScilab(Object value) {
                Mat image = (Mat)value;

                Mat converted = new Mat();
                image.convertTo(converted, CvType.makeType(4, 3));
                int[] data = new int[converted.width() * converted.height() * 3];
                converted.get(0, 0, data);
                data[data.length - 2] = converted.width();
                data[data.length - 1] = converted.height();

                return new ScilabInteger(new int[][] { data, new int[] { converted.width() }, new int[] { converted.height() } }, false);
            }

            @Override
            public Object fromScilabToJava(ScilabType value) {
                if(value != null && value.getType() == ScilabTypeEnum.sci_ints) {
                    int[][] data = ((ScilabInteger)value).getDataAsInt();
                    Mat image = new Mat(data[2][0], data[1][0], CvType.makeType(4, 3));
                    image.put(0, 0, data[0]);
                    Mat converted = new Mat();
                    image.convertTo(converted, CvType.makeType(4, 3));
                    return image;
                }

                return null;
            }
        });
    }

    public static SciConverter getConverterForType(DataType type) {
        return converters.get(type);
    }
}
