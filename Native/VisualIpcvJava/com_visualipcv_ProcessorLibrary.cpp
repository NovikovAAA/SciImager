#include "pch.h"
#include "PluginsLoader.hpp"
#include "com_visualipcv_ProcessorLibrary.h"
#include <VisualIPCV/ProcessorManager.hpp>

using namespace std;
using namespace std::filesystem;

extern "C"
{

    JNIEXPORT void JNICALL Java_com_visualipcv_core_ProcessorLibrary_loadPlugins(JNIEnv* env, jclass clazz, jstring path)
    {
        std::string pathStr = env->GetStringUTFChars(path, 0);
        PluginsLoader loader;
        loader.loadPlugins(pathStr, false);
    }

    JNIEXPORT void JNICALL Java_com_visualipcv_core_ProcessorLibrary_loadPluginsWithManualRegister(JNIEnv* env, jclass clazz, jstring path)
    {
        std::string pathStr = env->GetStringUTFChars(path, 0);
        PluginsLoader loader;
        loader.loadPlugins(pathStr, true);
    }


	JNIEXPORT jobject JNICALL Java_com_visualipcv_core_ProcessorLibrary_getProcessorList(JNIEnv* env, jclass clazz)
	{
        
		jclass arrayClass = env->FindClass("java/util/ArrayList");
		assert(arrayClass != nullptr);

		jmethodID arrayConstructor = env->GetMethodID(arrayClass, "<init>", "()V");
		assert(arrayConstructor != nullptr);

		jmethodID arrayAdd = env->GetMethodID(arrayClass, "add", "(Ljava/lang/Object;)Z");
		assert(arrayAdd != nullptr);

        jobject res = env->NewObject(arrayClass, arrayConstructor);
        
		jclass processorUidClass = env->FindClass("com/visualipcv/core/ProcessorUID");
		assert(processorUidClass != nullptr);

		jmethodID processorConstructor = env->GetMethodID(processorUidClass, "<init>", "(Ljava/lang/String;Ljava/lang/String;)V");
		assert(processorConstructor != nullptr);
        
        std::map<std::string, std::map<std::string, Processor *>>& modules = ProcessorManager::getModules();
        for (auto& moduleItem : modules) {
            std::map<std::string, Processor *> processors = moduleItem.second;
            for (auto& processorItem : processors) {
                Processor *processor = processorItem.second;
                std::string processorName = processor->name;
                std::string moduleName = processor->module;
                
                jstring processorNameStr = env->NewStringUTF(processorName.c_str());
                jstring moduleNameStr = env->NewStringUTF(moduleName.c_str());
                
                jobject proc = env->NewObject(processorUidClass, processorConstructor, processorNameStr, moduleNameStr);
                env->CallVoidMethod(res, arrayAdd, proc);
            }
        }
		return res;
	}
}
