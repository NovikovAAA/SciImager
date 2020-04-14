//
//  DataTypeJNIObject.cpp
//  VisualIpcvJava
//
//  Created by Артём Новиков on 14.04.2020.
//

#include "DataTypeJNIObject.hpp"

DataTypeJNIObject::DataTypeJNIObject(PrimitiveTypeСlassifier classifier, jclass _dataTypeClass, jmethodID _dataTypeConstructor, jmethodID _dataTypeGetValueMethod) {
    dataTypeClass = _dataTypeClass;
    dataTypeConstructor = _dataTypeConstructor;
    dataTypeGetValueMethod = _dataTypeGetValueMethod;
}
