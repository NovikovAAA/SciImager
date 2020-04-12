//
//  DataTypeJNIModel.h
//  VisualIpcvJava
//
//  Created by Артём Новиков on 12.04.2020.
//

#ifndef DataTypeJNIModel_h
#define DataTypeJNIModel_h

#include "jni.h"

struct DataTypeJNIModel {
    jclass dataTypeClass;
    jmethodID dataTypeConstructor;
    jmethodID dataTypeGetValueMethod;
};


#endif /* DataTypeJNIModel_h */
