#pragma once

extern "C"
{
	JNIEXPORT jobject JNICALL Java_com_visualipcv_NativeProcessor_getInputPropertyList(JNIEnv* env, jclass clazz, jobject uid);
	JNIEXPORT jobject JNICALL Java_com_visualipcv_NativeProcessor_getOutputPropertyList(JNIEnv* env, jclass clazz, jobject uid);
	JNIEXPORT jstring JNICALL Java_com_visualipcv_NativeProcessor_getCategory(JNIEnv* env, jclass clazz, jobject uid);
	JNIEXPORT jobject JNICALL Java_com_visualipcv_NativeProcessor_execute(JNIEnv* env, jclass clazz, jobject uid, jobject inputs);
}