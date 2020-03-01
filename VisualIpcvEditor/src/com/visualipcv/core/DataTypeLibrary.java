package com.visualipcv.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataTypeLibrary {
    private static List<DataType> dataTypes = new ArrayList<>();
    private static native DataType[] getDataTypeList();

    public static List<DataType> getDataTypes() {
        return dataTypes;
    }

    public static DataType getByName(String name) {
        for(DataType dataType : dataTypes) {
            if(dataType.getName().equals(name)) {
                return dataType;
            }
        }
        return null;
    }

    public static void initDefaultTypes() {
        dataTypes.add(DataType.NUMBER);
        dataTypes.add(DataType.VECTOR2);
        dataTypes.add(DataType.VECTOR3);
        dataTypes.add(DataType.VECTOR4);
        dataTypes.add(DataType.IMAGE);
        dataTypes.add(DataType.STRING);
        dataTypes.add(DataType.BYTES);
    }
}
