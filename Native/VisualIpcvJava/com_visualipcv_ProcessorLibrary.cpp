#include "pch.h"
#include "com_visualipcv_ProcessorLibrary.h"

extern "C"
{
	JNIEXPORT jobjectArray JNICALL Java_com_visualipcv_ProcessorLibrary_getProcessorList(JNIEnv* env, jobject object)
	{
		jclass mapClass = env->FindClass("java/util/HashMap");
		assert(mapClass != nullptr);

		jclass procClass = env->FindClass("com/visualipcv/Processor");
		assert(procClass != nullptr);

		jmethodID mapConstructor = env->GetMethodID(mapClass, "<init>", "()V");
		assert(mapConstructor != nullptr);

		jmethodID procConstructor = env->GetMethodID(procClass, "<init>", "(Ljava/lang/String;Ljava/util/Map;Ljava/util/Map;)V");
		assert(procConstructor != nullptr);

		jobjectArray procs = env->NewObjectArray(1000, procClass, nullptr);

		for (int i = 0; i < 1000; i++)
		{
			std::wstring tmp = L"Test" + std::to_wstring(i);
			jstring name = env->NewString((const jchar*)tmp.c_str(), (jsize)tmp.size());
			
			jobject inputProperties = env->NewObject(mapClass, mapConstructor);
			jobject outputProperties = env->NewObject(mapClass, mapConstructor);

			jobject proc = env->NewObject(procClass, procConstructor, name, inputProperties, outputProperties);
			env->SetObjectArrayElement(procs, i, proc);
		}

		return procs;
	}
}
