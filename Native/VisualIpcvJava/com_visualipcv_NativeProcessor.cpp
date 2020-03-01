#include "pch.h"
#include "com_visualipcv_NativeProcessor.h"
#include <VisualIPCV/ProcessorManager.hpp>
#include "JNIHelper.hpp"

extern "C"
{
	JNIEXPORT jobject JNICALL Java_com_visualipcv_core_NativeProcessor_getInputPropertyList(JNIEnv* env, jclass clazz, jobject uid)
	{
        Processor *processor = JNIHelper::processorFromJNI(env, uid);
        return JNIHelper::propertiesForJNI(env, uid, processor -> inputProperties);
	}

	JNIEXPORT jobject JNICALL Java_com_visualipcv_core_NativeProcessor_getOutputPropertyList(JNIEnv* env, jclass clazz, jobject uid)
	{
		Processor *processor = JNIHelper::processorFromJNI(env, uid);
        return JNIHelper::propertiesForJNI(env, uid, processor -> outputProperties);
	}

	JNIEXPORT jstring JNICALL Java_com_visualipcv_core_NativeProcessor_getCategory(JNIEnv* env, jclass clazz, jobject uid)
	{
        std::string category = JNIHelper::processorFromJNI(env, uid) -> category;
		return env->NewStringUTF(category.c_str());
	}

	JNIEXPORT jobject JNICALL Java_com_visualipcv_core_NativeProcessor_execute(JNIEnv* env, jclass clazz, jobject uid, jobject inputs)
	{
		jclass arrayClass = env->FindClass("java/util/ArrayList");
		assert(arrayClass != nullptr);

		jmethodID arrayConstructor = env->GetMethodID(arrayClass, "<init>", "()V");
		assert(arrayConstructor != nullptr);

		jmethodID arrayAdd = env->GetMethodID(arrayClass, "add", "(Ljava/lang/Object;)Z");
		assert(arrayAdd != nullptr);

		jobject res = env->NewObject(arrayClass, arrayConstructor);
		env->CallVoidMethod(res, arrayAdd, nullptr);
		env->CallVoidMethod(res, arrayAdd, nullptr);
		env->CallVoidMethod(res, arrayAdd, nullptr);

		return res;
	}
}
