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
#include <memory>
#include <VisualIPCV/BaseDataTypeClassifier.hpp>
#include "PrimitiveTypeClassifier.hpp"
#include "DataTypeJNIObject.hpp"
#include "jni.h"

using namespace std;

class DataTypesManager {
public:
    static DataTypesManager& getInstance() {
        static DataTypesManager instance;
        return instance;
    }
    std::unique_ptr<DataTypeJNIObject> getPrimitiveType(JNIEnv* env, jobject object);
    std::unique_ptr<DataTypeJNIObject> getPrimitiveType(JNIEnv* env, BaseDataTypeClassifier dataTypeClassifier);
private:
    DataTypesManager();
    DataTypesManager(const DataTypesManager&);
    DataTypesManager& operator=(DataTypesManager&);
    
    vector<PrimitiveTypeClassifier> classifiers;
    
    string javaTypeNameForClassifier(PrimitiveTypeClassifier classifier);
    jmethodID dataTypeConstructorForClassifier(JNIEnv* env, jclass typeClass, PrimitiveTypeClassifier classifier);
    jmethodID dataTypeGetValueMethodForClassifier(JNIEnv* env, jclass typeClass, PrimitiveTypeClassifier classifier);
    
    bool checkType(JNIEnv* env, PrimitiveTypeClassifier classifier, jobject object);
    
    std::unique_ptr<DataTypeJNIObject> createPrimitiveType(JNIEnv* env, PrimitiveTypeClassifier classifier);
    
    PrimitiveTypeClassifier primitiveTypeClassifier(BaseDataTypeClassifier dataTypeClassifier);
};

#endif /* DataTypesManager_hpp */
