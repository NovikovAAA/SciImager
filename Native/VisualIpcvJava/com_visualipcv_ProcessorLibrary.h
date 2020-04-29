#pragma once

extern "C"
{
    JNIEXPORT void JNICALL Java_com_visualipcv_core_ProcessorLibrary_loadPlugins(JNIEnv* env, jclass clazz, jstring path);
    JNIEXPORT void JNICALL Java_com_visualipcv_core_ProcessorLibrary_loadPluginsWithManualRegister(JNIEnv* env, jclass clazz, jstring path);
	JNIEXPORT jobject JNICALL Java_com_visualipcv_core_ProcessorLibrary_getProcessorList(JNIEnv* env,  jclass clazz);
}
