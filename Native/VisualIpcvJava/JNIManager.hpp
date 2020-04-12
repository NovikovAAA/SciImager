//
//  JNIHelper.hpp
//  VisualIpcvJava
//
//  Created by Артём Новиков on 01.03.2020.
//

#ifndef JNIHelper_hpp
#define JNIHelper_hpp

#include <stdio.h>
#include <VisualIPCV/ProcessorManager.hpp>
#include "DataBundleJNIModel.hpp"
#include "DataTypeJNIModel.hpp"
#include "jni.h"

class JNIManager {
public:
    static Processor* processorFromJava(JNIEnv* env, jobject uid);
    static DataBundle dataBundleFromJava(JNIEnv *env, jobject inputValues);
    
    static jobject propertiesForJava(JNIEnv* env, jobject uid, std::vector<ProcessorProperty> sourceProperties);
    static jobject processorResultForJava(JNIEnv *env, DataBundle result);
    static jobject dataBundleValuesObjectForJava(JNIEnv *env, jobject valuesObject);
private:
    static DataBundleJNIModel getDataBundleModel(JNIEnv *env);
    static DataTypeJNIModel getDataTypeModel(JNIEnv *env);
};

#endif /* JNIHelper_hpp */
