#include "pch.h"
#include "com_visualipcv_ProcessorLibrary.h"
#include <VisualIPCV/ProcessorManager.hpp>

extern "C"
{
	JNIEXPORT jobject JNICALL Java_com_visualipcv_core_ProcessorLibrary_getProcessorList(JNIEnv* env, jclass clazz)
	{
		jclass arrayClass = env->FindClass("java/util/ArrayList");
		assert(arrayClass != nullptr);

		jmethodID arrayConstructor = env->GetMethodID(arrayClass, "<init>", "()V");
		assert(arrayConstructor != nullptr);

		jmethodID arrayAdd = env->GetMethodID(arrayClass, "add", "(Ljava/lang/Object;)Z");
		assert(arrayAdd != nullptr);

		jclass processorUidClass = env->FindClass("com/visualipcv/core/ProcessorUID");
		assert(processorUidClass != nullptr);

		jmethodID processorConstructor = env->GetMethodID(processorUidClass, "<init>", "(Ljava/lang/String;Ljava/lang/String;)V");
		assert(processorConstructor != nullptr);

		jobject res = env->NewObject(arrayClass, arrayConstructor);

		for (int i = 0; i < 100; i++)
		{
			std::wstring name = L"Processor " + std::to_wstring(i);
			jstring nameStr = env->NewString((jchar*)name.c_str(), name.size());
			std::wstring modul = L"Core";
			jstring moduleStr = env->NewString((jchar*)modul.c_str(), modul.size());

			jobject proc = env->NewObject(processorUidClass, processorConstructor, nameStr, moduleStr);
			env->CallVoidMethod(res, arrayAdd, proc);
		}

		return res;
	}
}
