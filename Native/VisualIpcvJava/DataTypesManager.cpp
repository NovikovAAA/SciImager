//
//  DataTypesManager.cpp
//  VisualIpcvJava
//
//  Created by Артём Новиков on 14.04.2020.
//

#include "DataTypesManager.hpp"

DataTypesManager::DataTypesManager() {
    classifiers = {DOUBLE, STRING, IMAGE, UNKNOWN};
}

DataTypeJNIObject* DataTypesManager::getPrimitiveType(JNIEnv* env, jobject object) {
    for (int i = 0; i < classifiers.size(); i++) {
        if (checkType(env, classifiers[i], object)) {
            return createPrimitiveType(env, classifiers[i]);
        }
    }
    return nullptr;
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
    return new DataTypeJNIObject(classifier, typeClass, dataTypeConstructorForClassifier(env, typeClass, classifier), dataTypeGetValueMethodForClassifier(env, typeClass, classifier));
}

string DataTypesManager::javaTypeNameForClassifier(PrimitiveTypeСlassifier classifier) {
    switch (classifier) {
        case DOUBLE:
            return "java/lang/Double";
        case STRING:
            return "java/lang/String";
        default:
            return "java/lang/Object";
    }
}

jmethodID DataTypesManager::dataTypeConstructorForClassifier(JNIEnv* env, jclass typeClass,PrimitiveTypeСlassifier classifier) {
    string constructorString;
    string signatureString;
    switch (classifier) {
        case DOUBLE:
            constructorString = "<init>";
            signatureString = "(D)V";
            break;
        default:
            constructorString = "";
            signatureString = "";
            break;
    }
    return env->GetMethodID(typeClass, constructorString.c_str(), signatureString.c_str());
}

jmethodID DataTypesManager::dataTypeGetValueMethodForClassifier(JNIEnv* env, jclass typeClass, PrimitiveTypeСlassifier classifier) {
    string getValueString;
    string signatureString;
    switch (classifier) {
        case DOUBLE:
            getValueString = "doubleValue";
            signatureString = "()D";
            break;
        default:
            getValueString = "";
            signatureString = "";
            break;
    }
    return env->GetMethodID(typeClass, getValueString.c_str(), signatureString.c_str());
}
