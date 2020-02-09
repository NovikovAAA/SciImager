#include "pch.h"
#include "com_visualipcv_NativeProcessor.h"

extern "C"
{
	JNIEXPORT jobject JNICALL Java_com_visualipcv_core_NativeProcessor_getInputPropertyList(JNIEnv* env, jclass clazz, jobject uid)
	{
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

		std::wstring numberStr = L"Number";
		std::wstring vector2Str = L"Vector2";
		std::wstring vector3Str = L"Vector3";
		std::wstring vector4Str = L"Vector4";
		std::wstring fileStr = L"File";
		std::wstring imageStr = L"Image";

		jstring numberName = env->NewString((jchar*)numberStr.c_str(), (jsize)numberStr.size());
		jstring vector2Name = env->NewString((jchar*)vector2Str.c_str(), (jsize)vector2Str.size());
		jstring vector3Name = env->NewString((jchar*)vector3Str.c_str(), (jsize)vector3Str.size());
		jstring vector4Name = env->NewString((jchar*)vector4Str.c_str(), (jsize)vector4Str.size());
		jstring fileName = env->NewString((jchar*)fileStr.c_str(), (jsize)fileStr.size());
		jstring imageName = env->NewString((jchar*)imageStr.c_str(), (jsize)imageStr.size());

		jclass dataTypeLibrary = env->FindClass("com/visualipcv/core/DataTypeLibrary");
		assert(dataTypeLibrary != nullptr);

		jmethodID findTypeByName = env->GetStaticMethodID(dataTypeLibrary, "getByName", "(Ljava/lang/String;)Lcom/visualipcv/core/DataType;");
		assert(findTypeByName != nullptr);

		jobject imageType = env->CallStaticObjectMethod(dataType, findTypeByName, imageName);
		assert(imageType != nullptr);

		jobject numberType = env->CallStaticObjectMethod(dataType, findTypeByName, numberName);
		assert(numberType != nullptr);

		jobject vector2Type = env->CallStaticObjectMethod(dataType, findTypeByName, vector2Name);
		assert(vector2Type != nullptr);

		jobject vector3Type = env->CallStaticObjectMethod(dataType, findTypeByName, vector3Name);
		assert(vector3Type != nullptr);

		jobject vector4Type = env->CallStaticObjectMethod(dataType, findTypeByName, vector4Name);
		assert(vector4Type != nullptr);

		std::wstring in1Src = L"Input arg 1";
		std::wstring in2Src = L"Input arg 2";
		std::wstring in3Src = L"Input arg 3";
		std::wstring in4Src = L"Input arg 4";
		std::wstring in5Src = L"Input arg 5";

		jstring in1 = env->NewString((const jchar*)in1Src.c_str(), (jsize)in1Src.size());
		jstring in2 = env->NewString((const jchar*)in2Src.c_str(), (jsize)in2Src.size());
		jstring in3 = env->NewString((const jchar*)in3Src.c_str(), (jsize)in3Src.size());
		jstring in4 = env->NewString((const jchar*)in4Src.c_str(), (jsize)in4Src.size());
		jstring in5 = env->NewString((const jchar*)in5Src.c_str(), (jsize)in5Src.size());

		jobject inProp1 = env->NewObject(propertyClass, propertyConstructor, in1, numberType);
		jobject inProp2 = env->NewObject(propertyClass, propertyConstructor, in2, imageType);
		jobject inProp3 = env->NewObject(propertyClass, propertyConstructor, in3, vector2Type);
		jobject inProp4 = env->NewObject(propertyClass, propertyConstructor, in4, vector3Type);
		jobject inProp5 = env->NewObject(propertyClass, propertyConstructor, in5, vector4Type);

		jobject inputProperties = env->NewObject(arrayClass, arrayConstructor);
		env->CallVoidMethod(inputProperties, arrayAdd, inProp1);
		env->CallVoidMethod(inputProperties, arrayAdd, inProp2);
		env->CallVoidMethod(inputProperties, arrayAdd, inProp3);
		env->CallVoidMethod(inputProperties, arrayAdd, inProp4);
		env->CallVoidMethod(inputProperties, arrayAdd, inProp5);

		return inputProperties;
	}

	JNIEXPORT jobject JNICALL Java_com_visualipcv_core_NativeProcessor_getOutputPropertyList(JNIEnv* env, jclass clazz, jobject uid)
	{
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

		std::wstring numberStr = L"Number";
		std::wstring vector2Str = L"Vector2";
		std::wstring vector3Str = L"Vector3";
		std::wstring vector4Str = L"Vector4";
		std::wstring fileStr = L"File";
		std::wstring imageStr = L"Image";

		jstring numberName = env->NewString((jchar*)numberStr.c_str(), (jsize)numberStr.size());
		jstring vector2Name = env->NewString((jchar*)vector2Str.c_str(), (jsize)vector2Str.size());
		jstring vector3Name = env->NewString((jchar*)vector3Str.c_str(), (jsize)vector3Str.size());
		jstring vector4Name = env->NewString((jchar*)vector4Str.c_str(), (jsize)vector4Str.size());
		jstring fileName = env->NewString((jchar*)fileStr.c_str(), (jsize)fileStr.size());
		jstring imageName = env->NewString((jchar*)imageStr.c_str(), (jsize)imageStr.size());

		jclass dataTypeLibrary = env->FindClass("com/visualipcv/core/DataTypeLibrary");
		assert(dataTypeLibrary != nullptr);

		jmethodID findTypeByName = env->GetStaticMethodID(dataTypeLibrary, "getByName", "(Ljava/lang/String;)Lcom/visualipcv/core/DataType;");
		assert(findTypeByName != nullptr);

		jobject imageType = env->CallStaticObjectMethod(dataType, findTypeByName, imageName);
		assert(imageType != nullptr);

		jobject numberType = env->CallStaticObjectMethod(dataType, findTypeByName, numberName);
		assert(numberType != nullptr);

		jobject vector2Type = env->CallStaticObjectMethod(dataType, findTypeByName, vector2Name);
		assert(vector2Type != nullptr);

		jobject vector3Type = env->CallStaticObjectMethod(dataType, findTypeByName, vector3Name);
		assert(vector3Type != nullptr);

		jobject vector4Type = env->CallStaticObjectMethod(dataType, findTypeByName, vector4Name);
		assert(vector4Type != nullptr);

		std::wstring out1Src = L"Output arg 1";
		std::wstring out2Src = L"Output arg 2";
		std::wstring out3Src = L"Output arg 3";

		jstring out1 = env->NewString((const jchar*)out1Src.c_str(), (jsize)out1Src.size());
		jstring out2 = env->NewString((const jchar*)out2Src.c_str(), (jsize)out2Src.size());
		jstring out3 = env->NewString((const jchar*)out3Src.c_str(), (jsize)out3Src.size());

		jobject outProp1 = env->NewObject(propertyClass, propertyConstructor, out1, imageType);
		jobject outProp2 = env->NewObject(propertyClass, propertyConstructor, out2, numberType);
		jobject outProp3 = env->NewObject(propertyClass, propertyConstructor, out3, numberType);

		jobject outputProperties = env->NewObject(arrayClass, arrayConstructor);
		env->CallVoidMethod(outputProperties, arrayAdd, outProp1);
		env->CallVoidMethod(outputProperties, arrayAdd, outProp2);
		env->CallVoidMethod(outputProperties, arrayAdd, outProp3);

		return outputProperties;
	}

	JNIEXPORT jstring JNICALL Java_com_visualipcv_core_NativeProcessor_getCategory(JNIEnv* env, jclass clazz, jobject uid)
	{
		jclass uidClass = env->FindClass("com/visualipcv/core/ProcessorUID");
		assert(uidClass != nullptr);

		std::wstring category = L"Native processors";
		return env->NewString((jchar*)category.c_str(), category.size());
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