//
//  DataTypeJNIObject.hpp
//  VisualIpcvJava
//
//  Created by Артём Новиков on 14.04.2020.
//

#ifndef DataTypeJNIObject_hpp
#define DataTypeJNIObject_hpp

#include <stdio.h>
#include "jni.h"
#include "PrimitiveTypeClassifier.hpp"

class DataTypeJNIObject {
public:
    DataTypeJNIObject() {}
    DataTypeJNIObject(PrimitiveTypeClassifier _classifier, jclass dataTypeClass, jmethodID dataTypeConstructor, jmethodID dataTypeGetValueMethod);
    
    PrimitiveTypeClassifier classifier;
    
    jclass dataTypeClass;
    jmethodID dataTypeConstructor;
    jmethodID dataTypeGetValueMethod;
};


#endif /* DataTypeJNIObject_hpp */
