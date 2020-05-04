package com.visualipcv.core;

import com.visualipcv.core.annotations.PrimaryDataType;
import com.visualipcv.core.annotations.RegisterDataType;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class DataTypeLibrary {
    private static List<DataType> dataTypes = new ArrayList<>();
    private static native DataType[] getDataTypeList();

    public static List<DataType> getDataTypes() {
        return dataTypes;
    }

    private static final HashMap<Class<?>, Class<?>> TO_BOXED_TYPE = new HashMap<Class<?>, Class<?>>() {
        {
            put(int.class, Integer.class);
            put(double.class, Double.class);
        }
    };

    public static DataType getByName(String name) {
        for(DataType dataType : dataTypes) {
            if(dataType.getName().equals(name)) {
                return dataType;
            }
        }
        return null;
    }

    private static Class<?> getBoxedClass(Class<?> clazz) {
        Class<?> boxed = TO_BOXED_TYPE.get(clazz);

        if(boxed == null)
            return clazz;

        return boxed;
    }

    public static List<DataType> getFromClass(Class<?> clazz) {
        List<DataType> dataTypes = new ArrayList<>();
        Class<?> boxed = getBoxedClass(clazz);

        for(DataType type : DataTypeLibrary.dataTypes) {
            if(type.getClazz() == boxed) {
                dataTypes.add(type);
            }
        }

        return dataTypes;
    }

    public static DataType getUniqueFromClass(Class<?> clazz) throws CommonException {
        List<DataType> dataTypes = getFromClass(clazz);

        if(dataTypes.isEmpty())
            throw new CommonException("Get unique data type failed for " + clazz.getName());

        for(DataType type : dataTypes) {
            if(type.isPrimary())
                return type;
        }

        if(dataTypes.size() > 1)
            throw new CommonException("Get unique data type failed for " + clazz.getName());

        return dataTypes.get(0);
    }

    public static void registerDataType(DataType type, boolean isPrimary) {
        dataTypes.add(type);

        if(isPrimary)
            type.setPrimary();
    }

    public static void registerDataTypesFromPackage(String packageName) {
        Reflections reflections = new Reflections(packageName, new FieldAnnotationsScanner());
        Set<Field> fields = reflections.getFieldsAnnotatedWith(RegisterDataType.class);

        for(Field field : fields) {
            if(!Modifier.isStatic(field.getModifiers()))
                continue;

            if(field.getType() != DataType.class)
                continue;

            try {
                registerDataType((DataType)field.get(null), field.isAnnotationPresent(PrimaryDataType.class));
            } catch (Exception e) {

            }
        }
    }

    public static void init() {
        registerDataTypesFromPackage("com.visualipcv");
    }
}
