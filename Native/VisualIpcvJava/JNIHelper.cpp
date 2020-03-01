//
//  JNIHelper.cpp
//  VisualIpcvJava
//
//  Created by Артём Новиков on 01.03.2020.
//

#include "JNIHelper.hpp"

Processor* JNIHelper::processorFromJNI(JNIEnv *env, jobject uid) {
    jclass uidClass = env->FindClass("com/visualipcv/core/ProcessorUID");
    assert(uidClass != nullptr);

    jfieldID processNameFieldID = env->GetFieldID(uidClass, "name", "Ljava/lang/String;");
    jfieldID processModuleFieldID = env->GetFieldID(uidClass, "module", "Ljava/lang/String;");
    assert(processNameFieldID != nullptr);
    assert(processModuleFieldID != nullptr);

    jstring processNameObject = (jstring) env->GetObjectField(uid, processNameFieldID);
    jstring processModuleObject = (jstring) env->GetObjectField(uid, processModuleFieldID);
    assert(processNameObject != nullptr);
    assert(processModuleObject != nullptr);

    std::string nameStr = env->GetStringUTFChars(processNameObject, 0);
    std::string moduleStr = env->GetStringUTFChars(processModuleObject, 0);

    return ProcessorManager::find(moduleStr, nameStr);
}

jobject JNIHelper::propertiesForJNI(JNIEnv *env, jobject uid, std::vector<ProcessorProperty> sourceProperties) {
    jclass arrayClass = env->FindClass("java/util/ArrayList");
    assert(arrayClass != nullptr);

    jclass propertyClass = env->FindClass("com/visualipcv/core/ProcessorProperty");
    assert(propertyClass != nullptr);

    jclass processorUidClass = env->FindClass("com/visualipcv/core/ProcessorUID");
    assert(processorUidClass != nullptr);

    jmethodID arrayConstructor = env->GetMethodID(arrayClass, "<init>", "()V");
    assert(arrayConstructor != nullptr);

    jmethodID arrayAdd = env->GetMethodID(arrayClass, "add", "(Ljava/lang/Object;)Z");
    assert(arrayAdd != nullptr);

    jmethodID propertyConstructor = env->GetMethodID(propertyClass, "<init>", "(Ljava/lang/String;Lcom/visualipcv/core/DataType;)V");
    assert(propertyConstructor != nullptr);

    jclass dataType = env->FindClass("com/visualipcv/core/DataType");
    assert(dataType != nullptr);

    jclass dataTypeLibrary = env->FindClass("com/visualipcv/core/DataTypeLibrary");
    assert(dataTypeLibrary != nullptr);

    jmethodID findTypeByName = env->GetStaticMethodID(dataTypeLibrary, "getByName", "(Ljava/lang/String;)Lcom/visualipcv/core/DataType;");
    assert(findTypeByName != nullptr);

    jobject inputProperties = env->NewObject(arrayClass, arrayConstructor);

    for (int i = 0; i < sourceProperties.size(); i++) {
        std::string inputPropertyDataTypeNameString = sourceProperties[i].type.getName();
        std::string inputPropertyNameString = sourceProperties[i].name;
        
        jstring inputPropertyDataTypeName = env->NewStringUTF(inputPropertyDataTypeNameString.c_str());
        jstring inputPropertyName = env->NewStringUTF(inputPropertyNameString.c_str());
        
        jobject type = env->CallStaticObjectMethod(dataType, findTypeByName, inputPropertyDataTypeName);
        assert(type != nullptr);
        
        jobject inputProperty = env->NewObject(propertyClass, propertyConstructor, inputPropertyName, type);
        env->CallVoidMethod(inputProperties, arrayAdd, inputProperty);
    }
    return inputProperties;
}
