#include "pch.h"
#include "com_visualipcv_DataTypeLibrary.h"

extern "C"
{
	JNIEXPORT jobjectArray JNICALL Java_com_visualipcv_DataTypeLibrary_getDataTypeList(JNIEnv* env, jclass clazz)
	{
		jclass dataType = env->FindClass("com/visualipcv/DataType");
		assert(dataType != nullptr);

		jclass colorType = env->FindClass("java/awt/Color");
		assert(colorType != nullptr);

		jmethodID dataTypeConstructor = env->GetMethodID(dataType, "<init>", "(Ljava/lang/String;Ljava/awt/Color;)V");
		assert(dataTypeConstructor != nullptr);

		jmethodID colorConstructor = env->GetMethodID(colorType, "<init>", "(IIII)V");
		assert(colorConstructor != nullptr);

		jobjectArray types = env->NewObjectArray(6, dataType, nullptr);

		std::wstring numberStr = L"Number";
		std::wstring vector2Str = L"Vector2";
		std::wstring vector3Str = L"Vector3";
		std::wstring vector4Str = L"Vector4";
		std::wstring fileStr = L"File";
		std::wstring imageStr = L"Image";

		jstring numberName = env->NewString((jchar*)numberStr.c_str(), (int)numberStr.size());
		jobject numberColor = env->NewObject(colorType, colorConstructor, 0, 255, 0, 255);
		jobject number = env->NewObject(dataType, dataTypeConstructor, numberName, numberColor);

		jstring vector2Name = env->NewString((jchar*)vector2Str.c_str(), (int)vector2Str.size());
		jobject vector2Color = env->NewObject(colorType, colorConstructor, 197, 123, 63, 255);
		jobject vector2 = env->NewObject(dataType, dataTypeConstructor, vector2Name, vector2Color);

		jstring vector3Name = env->NewString((jchar*)vector3Str.c_str(), (int)vector3Str.size());
		jobject vector3Color = env->NewObject(colorType, colorConstructor, 197, 123, 63, 255);
		jobject vector3 = env->NewObject(dataType, dataTypeConstructor, vector3Name, vector3Color);

		jstring vector4Name = env->NewString((jchar*)vector4Str.c_str(), (int)vector4Str.size());
		jobject vector4Color = env->NewObject(colorType, colorConstructor, 197, 123, 63, 255);
		jobject vector4 = env->NewObject(dataType, dataTypeConstructor, vector4Name, vector4Color);

		jstring fileName = env->NewString((jchar*)fileStr.c_str(), (int)fileStr.size());
		jobject fileColor = env->NewObject(colorType, colorConstructor, 100, 0, 200, 255);
		jobject file = env->NewObject(dataType, dataTypeConstructor, fileName, fileColor);

		jstring imageName = env->NewString((jchar*)imageStr.c_str(), (int)imageStr.size());
		jobject imageColor = env->NewObject(colorType, colorConstructor, 255, 255, 255, 255);
		jobject image = env->NewObject(dataType, dataTypeConstructor, imageName, imageColor);

		env->SetObjectArrayElement(types, 0, number);
		env->SetObjectArrayElement(types, 1, vector2);
		env->SetObjectArrayElement(types, 2, vector3);
		env->SetObjectArrayElement(types, 3, vector4);
		env->SetObjectArrayElement(types, 4, file);
		env->SetObjectArrayElement(types, 5, image);

		return types;
	}
}