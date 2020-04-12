//
//  JNIHelper.cpp
//  VisualIpcvJava
//
//  Created by Артём Новиков on 01.03.2020.
//

#include "JNIManager.hpp"

Processor* JNIManager::processorFromJava(JNIEnv *env, jobject uid) {
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

jobject JNIManager::propertiesForJava(JNIEnv *env, jobject uid, std::vector<ProcessorProperty> sourceProperties) {
    jclass arrayClass = env->FindClass("java/util/ArrayList");
    assert(arrayClass != nullptr);

    jmethodID arrayConstructor = env->GetMethodID(arrayClass, "<init>", "()V");
    assert(arrayConstructor != nullptr);

    jmethodID arrayAdd = env->GetMethodID(arrayClass, "add", "(Ljava/lang/Object;)Z");
    assert(arrayAdd != nullptr);
    
    jclass propertyClass = env->FindClass("com/visualipcv/core/ProcessorProperty");
    assert(propertyClass != nullptr);

    jclass processorUidClass = env->FindClass("com/visualipcv/core/ProcessorUID");
    assert(processorUidClass != nullptr);

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

jobject JNIManager::processorResultForJava(JNIEnv *env, DataBundle result) {
    DataBundleJNIModel model = getDataBundleModel(env);
    DataTypeJNIModel typeModel = getDataTypeModel(env);
    
    jobject outputDataBundle = env->NewObject(model.dataBundleClass, model.dataBundleConstructor);
    assert(outputDataBundle != nullptr);
    
    for (auto& item : result.dataMap) {
        jstring key = env->NewStringUTF(item.first.c_str());
        jobject value = env->NewObject(typeModel.dataTypeClass, typeModel.dataTypeConstructor, result.read<double>(item.first));
        env->CallVoidMethod(outputDataBundle, model.dataBundleWriteMethod, key, value);
    }
    return outputDataBundle;
}

jobject JNIManager::dataBundleValuesObjectForJava(JNIEnv *env, jobject valuesObject) {
    DataBundleJNIModel model = getDataBundleModel(env);
    jobject dataMapObject = env->GetObjectField(valuesObject, model.dataMapValuesFieldID);
    assert(dataMapObject != nullptr);
    return dataMapObject;
}

DataBundle JNIManager::dataBundleFromJava(JNIEnv *env, jobject inputValues) {
    jclass mapClass = env->FindClass("Ljava/util/Map;");
    assert(mapClass != nullptr);
    jclass setClass = env->FindClass("Ljava/util/Set;");
    assert(setClass != nullptr);
    
    jmethodID mapGet = env->GetMethodID(mapClass, "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
    assert(mapGet != nullptr);
    jmethodID mapPut = env->GetMethodID(mapClass, "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
    assert(mapPut != nullptr);
    jmethodID mapKeySet = env->GetMethodID(mapClass, "keySet", "()Ljava/util/Set;");
    assert(mapKeySet != nullptr);
    jmethodID setToArray = env->GetMethodID(setClass, "toArray", "()[Ljava/lang/Object;");
    assert(setToArray != nullptr);
    
    jobject dataBundleValuesObject = dataBundleValuesObjectForJava(env, inputValues);
    
    jobject keySet = env->CallObjectMethod(dataBundleValuesObject, mapKeySet);
    assert(keySet != nullptr);
    jobjectArray keysArray = (jobjectArray)env->CallObjectMethod(keySet, setToArray);
    assert(keysArray != nullptr);
    
    DataTypeJNIModel typeModel = getDataTypeModel(env);
    
    DataBundle valuesBundle;
    int size = env-> GetArrayLength(keysArray);
    for (int i = 0; i < size; i++) {
        jstring keyString = (jstring) env->GetObjectArrayElement(keysArray, i);
        assert(keyString != nullptr);
        jobject dataValueObject = env->CallObjectMethod(dataBundleValuesObject, mapGet, keyString);
        assert(dataValueObject != nullptr);
        
        std::string key = env->GetStringUTFChars(keyString, 0);
        double value = (double) env->CallDoubleMethod(dataValueObject, typeModel.dataTypeGetValueMethod);
        valuesBundle.write(key, value);
    }
    return valuesBundle;
}

#pragma mark - Private

DataBundleJNIModel JNIManager::getDataBundleModel(JNIEnv *env) {
    jclass dataBundleClass = env->FindClass("com/visualipcv/core/DataBundle");
    assert(dataBundleClass != nullptr);
    jmethodID dataBundleConstructor = env->GetMethodID(dataBundleClass, "<init>", "()V");
    assert(dataBundleConstructor != nullptr);
    jfieldID dataMapValuesFieldID = env->GetFieldID(dataBundleClass, "values", "Ljava/util/Map;");
    assert(dataMapValuesFieldID != nullptr);
    jmethodID dataBundleWrite = env->GetMethodID(dataBundleClass, "write", "(Ljava/lang/String;Ljava/lang/Object;)V");
    assert(dataBundleWrite != nullptr);
    
    DataBundleJNIModel model;
    model.dataBundleClass = dataBundleClass;
    model.dataBundleConstructor = dataBundleConstructor;
    model.dataMapValuesFieldID = dataMapValuesFieldID;
    model.dataBundleWriteMethod = dataBundleWrite;
    
    return model;
}

#warning
// Расширить на стандартные типы данных
DataTypeJNIModel JNIManager::getDataTypeModel(JNIEnv *env) {
    jclass doubleClass = env->FindClass("Ljava/lang/Double;");
    assert(doubleClass != nullptr);
    jmethodID doubleConstructor = env->GetMethodID(doubleClass, "<init>", "(D)V");
    assert(doubleConstructor != nullptr);
    jmethodID getPrimitiveValueMethod = env->GetMethodID(doubleClass, "doubleValue", "()D");
    assert(getPrimitiveValueMethod != nullptr);
    
    DataTypeJNIModel typeModel;
    typeModel.dataTypeClass = doubleClass;
    typeModel.dataTypeConstructor = doubleConstructor;
    typeModel.dataTypeGetValueMethod = getPrimitiveValueMethod;
    
    return typeModel;
}
