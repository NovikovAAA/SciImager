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
    classifiers = {JNI_DOUBLE, JNI_INTEGER, JNI_STRING, JNI_DIRECTORY, JNI_FILE, JNI_IMAGE, JNI_VECTOR2, JNI_VECTOR3, JNI_VECTOR4, UNKNOWN};
}

std::unique_ptr<DataTypeJNIObject> DataTypesManager::getPrimitiveType(JNIEnv* env, jobject object) {
    for (int i = 0; i < classifiers.size(); i++) {
        if (checkType(env, classifiers[i], object)) {
            return createPrimitiveType(env, classifiers[i]);
        }
    }
    return nullptr;
}

std::unique_ptr<DataTypeJNIObject> DataTypesManager::getPrimitiveType(JNIEnv* env, BaseDataTypeClassifier dataTypeClassifier) {
    PrimitiveTypeClassifier classifier = primitiveTypeClassifier(dataTypeClassifier);
    return createPrimitiveType(env, classifier);
}

bool DataTypesManager::checkType(JNIEnv* env, PrimitiveTypeClassifier classifier, jobject object) {
    jclass typeClass = env->FindClass(javaTypeNameForClassifier(classifier).c_str());
    if (typeClass == nullptr) {
        return false;
    }
    return (bool)env->IsInstanceOf(object, typeClass);
}

std::unique_ptr<DataTypeJNIObject> DataTypesManager::createPrimitiveType(JNIEnv* env, PrimitiveTypeClassifier classifier) {
    string javaTypeName = javaTypeNameForClassifier(classifier);
    jclass typeClass = env->FindClass(javaTypeName.c_str());
    assert(typeClass != nullptr);
    
    jmethodID constructor = dataTypeConstructorForClassifier(env, typeClass, classifier);
    jmethodID getValueMethod = dataTypeGetValueMethodForClassifier(env, typeClass, classifier);
    
    return std::make_unique<DataTypeJNIObject>(classifier, typeClass, constructor, getValueMethod);
}

string DataTypesManager::javaTypeNameForClassifier(PrimitiveTypeClassifier classifier) {
    switch (classifier) {
        case JNI_INTEGER:
            return "java/lang/Integer";
        case JNI_DOUBLE:
            return "java/lang/Double";
        case JNI_STRING:
        case JNI_DIRECTORY:
        case JNI_FILE:
            return "java/lang/String";
        case JNI_IMAGE:
            return "org/opencv/core/Mat";
        case JNI_VECTOR2:
        case JNI_VECTOR3:
        case JNI_VECTOR4:
            return "[Ljava/lang/Double;";
        default:
            return "java/lang/Object";
    }
}

jmethodID DataTypesManager::dataTypeConstructorForClassifier(JNIEnv* env, jclass typeClass,PrimitiveTypeClassifier classifier) {
    string constructorString;
    string signatureString;
    switch (classifier) {
        case JNI_INTEGER:
            constructorString = "<init>";
            signatureString = "(I)V";
            break;
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

jmethodID DataTypesManager::dataTypeGetValueMethodForClassifier(JNIEnv* env, jclass typeClass, PrimitiveTypeClassifier classifier) {
    string getValueString;
    string signatureString;
    switch (classifier) {
        case JNI_INTEGER:
            getValueString = "intValue";
            signatureString = "()I";
            break;
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

PrimitiveTypeClassifier DataTypesManager::primitiveTypeClassifier(BaseDataTypeClassifier dataTypeClassifier) {
    switch (dataTypeClassifier) {
        case BaseDataTypeClassifier::DOUBLE:
            return JNI_DOUBLE;
        case BaseDataTypeClassifier::INTEGER:
            return JNI_INTEGER;
        case BaseDataTypeClassifier::STRING:
        case BaseDataTypeClassifier::FILE:
        case BaseDataTypeClassifier::DIRECTORY:
            return JNI_STRING;
        case BaseDataTypeClassifier::IMAGE:
            return JNI_IMAGE;
        case BaseDataTypeClassifier::VECTOR2:
            return JNI_VECTOR2;
        case BaseDataTypeClassifier::VECTOR3:
            return JNI_VECTOR3;
        case BaseDataTypeClassifier::VECTOR4:
            return JNI_VECTOR4;
        default:
            return UNKNOWN;
    }
}
