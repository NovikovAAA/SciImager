#include "pch.h"
#include "com_visualipcv_NativeProcessor.h"
#include <VisualIPCV/ProcessorManager.hpp>
#include <VisualIPCV/DataBundle.hpp>
#include <VisualIPCV/Logger.hpp>
#include "JNIManager.hpp"

extern "C"
{
	JNIEXPORT jobject JNICALL Java_com_visualipcv_core_NativeProcessor_getInputPropertyList(JNIEnv* env, jclass clazz, jobject uid)
	{
        Processor *processor = JNIManager::processorFromJava(env, uid);
        return JNIManager::propertiesForJava(env, uid, processor -> inputProperties);
	}

	JNIEXPORT jobject JNICALL Java_com_visualipcv_core_NativeProcessor_getOutputPropertyList(JNIEnv* env, jclass clazz, jobject uid)
	{
		Processor *processor = JNIManager::processorFromJava(env, uid);
        return JNIManager::propertiesForJava(env, uid, processor -> outputProperties);
	}

	JNIEXPORT jstring JNICALL Java_com_visualipcv_core_NativeProcessor_getCategory(JNIEnv* env, jclass clazz, jobject uid)
	{
        std::string category = JNIManager::processorFromJava(env, uid) -> category;
		return env->NewStringUTF(category.c_str());
	}

	JNIEXPORT jobject JNICALL Java_com_visualipcv_core_NativeProcessor_execute(JNIEnv* env, jclass clazz, jobject uid, jobject inputs)
	{
        DataBundle inputBundle = JNIManager::dataBundleFromJava(env, inputs);
        DataBundle nodeState;
        
        Processor *processor = JNIManager::processorFromJava(env, uid);
        DataBundle executionResult = processor->execute(inputBundle, nodeState);
        
        return JNIManager::processorResultForJava(env, executionResult);
	}
}
