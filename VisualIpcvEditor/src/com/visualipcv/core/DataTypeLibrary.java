package com.visualipcv.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataTypeLibrary {
    private static List<DataType> dataTypes = new ArrayList<>();
    private static native DataType[] getDataTypeList();

    static {

    }

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

    public static void registerDataType(DataType type) {
        dataTypes.add(type);
    }
}
