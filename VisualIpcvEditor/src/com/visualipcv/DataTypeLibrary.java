package com.visualipcv;

public class DataTypeLibrary {
    private static DataType[] dataTypes;
    private static native DataType[] getDataTypeList();

    static {
        dataTypes = getDataTypeList();
    }

    public static DataType[] getDataTypes() {
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
}
