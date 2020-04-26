//
//  DataTypesManager.hpp
//  VisualIpcvJava
//
//  Created by Артём Новиков on 14.04.2020.
//

#ifndef DataTypesManager_hpp
#define DataTypesManager_hpp

#include <stdio.h>
#include <string>
#include <vector>
#include <VisualIPCV/BaseDataTypeClassifier.hpp>
#include "PrimitiveTypeСlassifier.hpp"
#include "DataTypeJNIObject.hpp"
#include "jni.h"

using namespace std;

class DataTypesManager {
public:
    static DataTypesManager& getInstance() {
        static DataTypesManager instance;
        return instance;
    }
    DataTypeJNIObject* getPrimitiveType(JNIEnv* env, jobject object);
    DataTypeJNIObject* getPrimitiveType(JNIEnv* env, BaseDataTypeClassifier dataTypeClassifier);
private:
    DataTypesManager();
    DataTypesManager(const DataTypesManager&);
    DataTypesManager& operator=(DataTypesManager&);
    
    vector<PrimitiveTypeСlassifier> classifiers;
    
    string javaTypeNameForClassifier(PrimitiveTypeСlassifier classifier);
    jmethodID dataTypeConstructorForClassifier(JNIEnv* env, jclass typeClass, PrimitiveTypeСlassifier classifier);
    jmethodID dataTypeGetValueMethodForClassifier(JNIEnv* env, jclass typeClass, PrimitiveTypeСlassifier classifier);
    
    bool checkType(JNIEnv* env, PrimitiveTypeСlassifier classifier, jobject object);
    
    DataTypeJNIObject* createPrimitiveType(JNIEnv* env, PrimitiveTypeСlassifier classifier);
    
    PrimitiveTypeСlassifier primitiveTypeClassifier(BaseDataTypeClassifier dataTypeClassifier);
};

#endif /* DataTypesManager_hpp */
