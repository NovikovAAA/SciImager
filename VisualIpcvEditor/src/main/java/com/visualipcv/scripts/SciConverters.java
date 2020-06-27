package com.visualipcv.scripts;

import com.visualipcv.core.CommonException;
import com.visualipcv.core.DataType;
import com.visualipcv.core.DataTypes;
import com.visualipcv.core.OpenCvUtils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.scilab.modules.external_objects_java.ScilabJavaArray;
import org.scilab.modules.types.ScilabDouble;
import org.scilab.modules.types.ScilabInteger;
import org.scilab.modules.types.ScilabMList;
import org.scilab.modules.types.ScilabString;
import org.scilab.modules.types.ScilabType;
import org.scilab.modules.types.ScilabTypeEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SciConverters {
    private static final Map<DataType, SciConverter> converters = new HashMap<>();

    public static void load() {
        converters.put(DataTypes.DOUBLE, new SciConverter() {
            @Override
            public ScilabType fromJavaToScilab(String name, Object value) {
                return new ScilabDouble((Double)value);
            }
            @Override
            public Object fromScilabToJava(String name, ScilabType value) {
                if(value == null)
                    return 0.0;

                return ((ScilabDouble)value).getRealElement(0, 0);
            }
        });

        converters.put(DataTypes.INTEGER, new SciConverter() {
            @Override
            public ScilabType fromJavaToScilab(String name, Object value) {
                return new ScilabInteger((Integer)value);
            }

            @Override
            public Object fromScilabToJava(String name, ScilabType value) {
                if(value == null)
                    return 0;

                return ((ScilabInteger)value).getIntElement(0, 0);
            }
        });

        converters.put(DataTypes.VECTOR2, new SciConverter() {
            @Override
            public ScilabType fromJavaToScilab(String name, Object value) {
                Double[] values = (Double[])value;
                double[][] fuckingPrimitivesForFuckingJava = new double[1][2];

                for(int i = 0; i < values.length; i++)
                    fuckingPrimitivesForFuckingJava[0][i] = values[i];

                return new ScilabDouble(fuckingPrimitivesForFuckingJava);
            }
            @Override
            public Object fromScilabToJava(String name, ScilabType value) {
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
            public ScilabType fromJavaToScilab(String name, Object value) {
                Double[] values = (Double[])value;
                double[][] fuckingPrimitivesForFuckingJava = new double[1][3];

                for(int i = 0; i < values.length; i++)
                    fuckingPrimitivesForFuckingJava[0][i] = values[i];

                return new ScilabDouble(fuckingPrimitivesForFuckingJava);
            }
            @Override
            public Object fromScilabToJava(String name, ScilabType value) {
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
            public ScilabType fromJavaToScilab(String name, Object value) {
                Double[] values = (Double[])value;
                double[][] fuckingPrimitivesForFuckingJava = new double[1][4];

                for(int i = 0; i < values.length; i++)
                    fuckingPrimitivesForFuckingJava[0][i] = values[i];

                return new ScilabDouble(fuckingPrimitivesForFuckingJava);
            }
            @Override
            public Object fromScilabToJava(String name, ScilabType value) {
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
            public ScilabType fromJavaToScilab(String name, Object value) {
                return new ScilabString((String)value);
            }

            @Override
            public Object fromScilabToJava(String name, ScilabType value) {
                if(value == null)
                    return "";

                return ((ScilabString)value).getData()[0][0];
            }
        });

        converters.put(DataTypes.IMAGE, new SciConverter() {
            @Override
            public ScilabType fromJavaToScilab(String name, Object value) {
                Mat image = (Mat)value;
                Mat converted = null;

                if(image.channels() == 4)
                    converted = image;
                else if(image.channels() == 3) {
                    List<Mat> channels = new ArrayList<>();
                    Core.split(image, channels);

                    Mat alpha = new Mat(image.height(), image.width(), CvType.makeType(image.depth(), 1));
                    alpha.setTo(new Scalar(255.0, 255.0, 255.0, 255.0));
                    channels.add(alpha);

                    converted = new Mat();
                    Core.merge(channels, converted);
                } else {
                    throw new CommonException("Unsupported image format");
                }

                converted.convertTo(converted, CvType.CV_8UC4);

                byte[] data = new byte[converted.width() * converted.height() * 4];
                converted.get(0, 0, data);
                byte[] finalData = new byte[converted.width() * converted.height() * 4];

                for(int i = 0; i < converted.width(); ++i) {
                    for(int j = 0; j < converted.height(); ++j) {
                        int slice = converted.width() * converted.height();
                        finalData[slice * 2 + (i * converted.height() + j)] = data[(j * converted.width() + i) * 4 + 0];
                        finalData[slice * 1 + (i * converted.height() + j)] = data[(j * converted.width() + i) * 4 + 1];
                        finalData[slice * 0 + (i * converted.height() + j)] = data[(j * converted.width() + i) * 4 + 2];
                        finalData[slice * 3 + (i * converted.height() + j)] = data[(j * converted.width() + i) * 4 + 3];
                    }
                }

                SciRunner.set(name + "_W", new ScilabInteger(converted.width()));
                SciRunner.set(name + "_H", new ScilabInteger(converted.height()));
                return new ScilabInteger(new byte[][] { finalData }, true);
            }

            @Override
            public Object fromScilabToJava(String name, ScilabType value) {
                if(value != null && value.getType() == ScilabTypeEnum.sci_ints) {
                    byte[][] data = ((ScilabInteger)value).getDataAsByte();
                    int width = ((ScilabInteger)SciRunner.get(name + "_W")).getIntElement(0, 0);
                    int height = ((ScilabInteger)SciRunner.get(name + "_H")).getIntElement(0, 0);

                    byte[] finalData = new byte[width * height * 4];

                    for(int i = 0; i < width; ++i) {
                        for(int j = 0; j < height; ++j) {
                            int slice = width * height;
                            finalData[(j * width + i) * 4 + 0] = data[0][slice * 2 + (i * height + j)];
                            finalData[(j * width + i) * 4 + 1] = data[0][slice * 1 + (i * height + j)];
                            finalData[(j * width + i) * 4 + 2] = data[0][slice * 0 + (i * height + j)];
                            finalData[(j * width + i) * 4 + 3] = data[0][slice * 3 + (i * height + j)];
                        }
                    }

                    Mat image = new Mat(height, width, CvType.CV_8UC4);
                    image.put(0, 0, finalData);
                    Mat converted = new Mat();
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
