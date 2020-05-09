//
//  DataTypesManager.cpp
//  VisualIpcvJava
//
//  Created by Артём Новиков on 14.04.2020.
//

#include "DataTypesManager.hpp"
#include <VisualIPCV/Logger.hpp>
#include <cassert>

DataTypesManager::DataTypesManager() {
    classifiers = {JNI_DOUBLE, JNI_STRING, JNI_IMAGE, UNKNOWN};
}

DataTypeJNIObject* DataTypesManager::getPrimitiveType(JNIEnv* env, jobject object) {
    for (int i = 0; i < classifiers.size(); i++) {
        if (checkType(env, classifiers[i], object)) {
            return createPrimitiveType(env, classifiers[i]);
        }
    }
    return nullptr;
}

DataTypeJNIObject* DataTypesManager::getPrimitiveType(JNIEnv* env, BaseDataTypeClassifier dataTypeClassifier) {
    PrimitiveTypeСlassifier classifier = primitiveTypeClassifier(dataTypeClassifier);
    return createPrimitiveType(env, classifier);
}

bool DataTypesManager::checkType(JNIEnv* env, PrimitiveTypeСlassifier classifier, jobject object) {
    jclass typeClass = env->FindClass(javaTypeNameForClassifier(classifier).c_str());
    if (typeClass == nullptr) {
        return false;
    }
    return (bool)env->IsInstanceOf(object, typeClass);
}

DataTypeJNIObject* DataTypesManager::createPrimitiveType(JNIEnv* env, PrimitiveTypeСlassifier classifier) {
    string javaTypeName = javaTypeNameForClassifier(classifier);
    jclass typeClass = env->FindClass(javaTypeName.c_str());
    
    jmethodID constructor = dataTypeConstructorForClassifier(env, typeClass, classifier);
    jmethodID getValueMethod = dataTypeGetValueMethodForClassifier(env, typeClass, classifier);
    
    return new DataTypeJNIObject(classifier, typeClass, constructor, getValueMethod);
}

string DataTypesManager::javaTypeNameForClassifier(PrimitiveTypeСlassifier classifier) {
    switch (classifier) {
        case JNI_DOUBLE:
            return "java/lang/Double";
        case JNI_STRING:
            return "java/lang/String";
        case JNI_IMAGE:
            return "org/opencv/core/Mat";
        default:
            return "java/lang/Object";
    }
}

jmethodID DataTypesManager::dataTypeConstructorForClassifier(JNIEnv* env, jclass typeClass,PrimitiveTypeСlassifier classifier) {
    string constructorString;
    string signatureString;
    switch (classifier) {
        case JNI_DOUBLE:
            constructorString = "<init>";
            signatureString = "(D)V";
            break;
        case JNI_IMAGE:
            constructorString = "<init>";
            signatureString = "(J)V";
            break;
        default:
            return nullptr;
    }
    return env->GetMethodID(typeClass, constructorString.c_str(), signatureString.c_str());
}

jmethodID DataTypesManager::dataTypeGetValueMethodForClassifier(JNIEnv* env, jclass typeClass, PrimitiveTypeСlassifier classifier) {
    string getValueString;
    string signatureString;
    switch (classifier) {
        case JNI_DOUBLE:
            getValueString = "doubleValue";
            signatureString = "()D";
            break;
        case JNI_IMAGE:
            getValueString = "getNativeObjAddr";
            signatureString = "()J";
            break;
        default:
            return nullptr;
    }
    return env->GetMethodID(typeClass, getValueString.c_str(), signatureString.c_str());
}

PrimitiveTypeСlassifier DataTypesManager::primitiveTypeClassifier(BaseDataTypeClassifier dataTypeClassifier) {
    switch (dataTypeClassifier) {
        case BaseDataTypeClassifier::DOUBLE:
            return JNI_DOUBLE;
        case BaseDataTypeClassifier::INTEGER:
            return JNI_INTEGER;
        case BaseDataTypeClassifier::STRING:
            return JNI_STRING;
        case BaseDataTypeClassifier::IMAGE:
            return JNI_IMAGE;
        default:
            return UNKNOWN;
    }
}
