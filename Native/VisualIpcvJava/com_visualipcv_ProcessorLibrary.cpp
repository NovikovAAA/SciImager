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

		jclass dataTypeClass = env->FindClass("com/visualipcv/DataType");
		assert(dataTypeClass != nullptr);

		jmethodID mapConstructor = env->GetMethodID(mapClass, "<init>", "()V");
		assert(mapConstructor != nullptr);

		jmethodID procConstructor = env->GetMethodID(procClass, "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/util/Map;Ljava/util/Map;)V");
		assert(procConstructor != nullptr);

		jmethodID mapPut = env->GetMethodID(mapClass, "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
		assert(mapPut != nullptr);

		jfieldID imageType = env->GetStaticFieldID(dataTypeClass, "IMAGE", "Lcom/visualipcv/DataType;");
		assert(imageType != nullptr);

		jfieldID numberType = env->GetStaticFieldID(dataTypeClass, "NUMBER", "Lcom/visualipcv/DataType;");
		assert(numberType != nullptr);

		jobjectArray procs = env->NewObjectArray(1000, procClass, nullptr);

		for (int i = 0; i < 1000; i++)
		{
			std::wstring nameSrc = L"Test" + std::to_wstring(i);
			std::wstring catSrc = L"Category" + std::to_wstring(i / 10);
			std::wstring in1Src = L"Input arg 1";
			std::wstring in2Src = L"Input arg 2";
			std::wstring out1Src = L"Output arg 1";
			std::wstring out2Src = L"Output arg 2";
			std::wstring out3Src = L"Output arg 3";

			jstring name = env->NewString((const jchar*)nameSrc.c_str(), (jsize)nameSrc.size());
			jstring cat = env->NewString((const jchar*)catSrc.c_str(), (jsize)catSrc.size());
			jstring in1 = env->NewString((const jchar*)in1Src.c_str(), (jsize)in1Src.size());
			jstring in2 = env->NewString((const jchar*)in2Src.c_str(), (jsize)in2Src.size());
			jstring out1 = env->NewString((const jchar*)out1Src.c_str(), (jsize)out1Src.size());
			jstring out2 = env->NewString((const jchar*)out2Src.c_str(), (jsize)out2Src.size());
			jstring out3 = env->NewString((const jchar*)out3Src.c_str(), (jsize)out3Src.size());
			
			jobject inputProperties = env->NewObject(mapClass, mapConstructor);
			env->CallVoidMethod(inputProperties, mapPut, in1, env->GetStaticObjectField(dataTypeClass, imageType));
			env->CallVoidMethod(inputProperties, mapPut, in2, env->GetStaticObjectField(dataTypeClass, numberType));
			jobject outputProperties = env->NewObject(mapClass, mapConstructor);
			env->CallVoidMethod(outputProperties, mapPut, out1, env->GetStaticObjectField(dataTypeClass, imageType));
			env->CallVoidMethod(outputProperties, mapPut, out2, env->GetStaticObjectField(dataTypeClass, numberType));
			env->CallVoidMethod(outputProperties, mapPut, out3, env->GetStaticObjectField(dataTypeClass, numberType));

			jobject proc = env->NewObject(procClass, procConstructor, name, cat, inputProperties, outputProperties);
			env->SetObjectArrayElement(procs, i, proc);
		}

		return procs;
	}
}
