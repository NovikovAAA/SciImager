#include "pch.h"
#include "com_visualipcv_ProcessorLibrary.h"

extern "C"
{
	JNIEXPORT jobjectArray JNICALL Java_com_visualipcv_ProcessorLibrary_getProcessorList(JNIEnv* env, jobject object)
	{
		jclass arrayClass = env->FindClass("java/util/ArrayList");
		assert(arrayClass != nullptr);

		jclass procClass = env->FindClass("com/visualipcv/Processor");
		assert(procClass != nullptr);

		jclass propertyClass = env->FindClass("com/visualipcv/ProcessorProperty");
		assert(propertyClass != nullptr);

		jclass dataTypeClass = env->FindClass("com/visualipcv/DataType");
		assert(dataTypeClass != nullptr);

		jmethodID arrayConstructor = env->GetMethodID(arrayClass, "<init>", "()V");
		assert(arrayConstructor != nullptr);

		jmethodID procConstructor = env->GetMethodID(procClass, "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/util/ArrayList;Ljava/util/ArrayList;)V");
		assert(procConstructor != nullptr);

		jmethodID arrayAdd = env->GetMethodID(arrayClass, "add", "(Ljava/lang/Object;)Z");
		assert(arrayAdd != nullptr);

		jmethodID propertyConstructor = env->GetMethodID(propertyClass, "<init>", "(Ljava/lang/String;Lcom/visualipcv/DataType;)V");
		assert(propertyConstructor != nullptr);

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

			jobject image = env->GetStaticObjectField(dataTypeClass, imageType);
			jobject number = env->GetStaticObjectField(dataTypeClass, numberType);

			jobject inProp1 = env->NewObject(propertyClass, propertyConstructor, in1, image);
			jobject inProp2 = env->NewObject(propertyClass, propertyConstructor, in2, number);
			jobject outProp1 = env->NewObject(propertyClass, propertyConstructor, out1, image);
			jobject outProp2 = env->NewObject(propertyClass, propertyConstructor, out2, number);
			jobject outProp3 = env->NewObject(propertyClass, propertyConstructor, out3, number);
			
			jobject inputProperties = env->NewObject(arrayClass, arrayConstructor);
			env->CallVoidMethod(inputProperties, arrayAdd, inProp1);
			env->CallVoidMethod(inputProperties, arrayAdd, inProp2);
			jobject outputProperties = env->NewObject(arrayClass, arrayConstructor);
			env->CallVoidMethod(outputProperties, arrayAdd, outProp1);
			env->CallVoidMethod(outputProperties, arrayAdd, outProp2);
			env->CallVoidMethod(outputProperties, arrayAdd, outProp3);

			jobject proc = env->NewObject(procClass, procConstructor, name, cat, inputProperties, outputProperties);
			env->SetObjectArrayElement(procs, i, proc);
		}

		return procs;
	}
}
