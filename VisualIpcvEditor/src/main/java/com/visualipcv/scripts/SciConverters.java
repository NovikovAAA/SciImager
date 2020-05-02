package com.visualipcv.scripts;

import com.visualipcv.core.DataType;
import com.visualipcv.core.DataTypes;
import com.visualipcv.core.OpenCvUtils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.scilab.modules.types.ScilabDouble;
import org.scilab.modules.types.ScilabInteger;
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
                return ((ScilabDouble)value).getRealElement(0, 0);
            }
        });

        converters.put(DataTypes.VECTOR2, new SciConverter() {
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

        converters.put(DataTypes.VECTOR3, new SciConverter() {
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

        converters.put(DataTypes.VECTOR4, new SciConverter() {
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

        converters.put(DataTypes.IMAGE, new SciConverter() {
            @Override
            public ScilabType fromJavaToScilab(Object value) {
                Mat image = (Mat)value;

                if(OpenCvUtils.isUChar(image.type())) {
                    int[][] data = new int[image.height()][image.width()];

                    for(int i = 0; i < image.height(); i++) {
                        for(int j = 0; j < image.width(); j++) {
                            byte[] ch = new byte[3];
                            image.get(i, j, ch);
                            data[i][j] = ch[1];
                        }
                    }

                    return new ScilabInteger(data, false);
                }

                return null;
            }
            @Override
            public Object fromScilabToJava(ScilabType value) {
                if(value.getType() == ScilabTypeEnum.sci_ints) {
                    int[][] data = ((ScilabInteger)value).getDataAsInt();
                    Mat image = new Mat(data.length, data[0].length, CvType.CV_8UC1);

                    for(int i = 0; i < image.height(); i++) {
                        for(int j = 0; j < image.width(); j++) {
                            byte[] ch = new byte[1];
                            ch[0] = (byte)data[i][j];
                            image.put(i, j, ch);
                        }
                    }

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
