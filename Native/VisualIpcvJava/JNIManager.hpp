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
#include "DataTypeJNIObject.hpp"
#include "jni.h"

class JNIManager {
public:
    static JNIManager& getInstance() {
        static JNIManager instance;
        return instance;
    }
    Processor* processorFromJava(JNIEnv* env, jobject uid);
    DataBundle dataBundleFromJava(JNIEnv *env, jobject inputValues);
    
    jobject propertiesForJava(JNIEnv* env, jobject uid, std::vector<ProcessorProperty> sourceProperties);
    jobject processorResultForJava(JNIEnv *env, DataBundle result);
    jobject dataBundleValuesObjectForJava(JNIEnv *env, jobject valuesObject);
private:
    JNIManager() {}
    JNIManager(const JNIManager&);
    JNIManager& operator=(JNIManager&);
    
    void writeToBundle(JNIEnv* env, jobject object, DataBundle valuesBundle, std::string key);
    
    DataBundleJNIModel getDataBundleModel(JNIEnv *env);
    DataTypeJNIObject getDataTypeModel(JNIEnv *env);
};

#endif /* JNIHelper_hpp */
