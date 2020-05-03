//
//  LibrariesLoader.cpp
//  VisualIpcvJava
//
//  Created by Артём Новиков on 29.04.2020.
//

#include "pch.h"
#include "PluginsLoader.hpp"
#include <VisualIPCV/Logger.hpp>
#include <filesystem>

using namespace std::filesystem;

#ifdef __APPLE__

void PluginsLoader::loadPlugins(std::string pathString, bool withManualRegister) {
    for(auto& p: directory_iterator(pathString)) {
        path dylibPath = p.path();
        if (dylibPath.extension() == ".dylib") {
            void* dylibDiscriptor = dlopen(dylibPath.c_str(), RTLD_NOW | RTLD_GLOBAL);
            if (!dylibDiscriptor) {
                Logger::getInstance().log(std::string(dylibPath.c_str()) + " library loading error");
                continue;
            }
            
            if (withManualRegister) {
                manualRegister(dylibDiscriptor);
            }
        }
    }
}

#pragma mark - Private

void PluginsLoader::manualRegister(void* dylibDiscriptor) {
    auto pluginInit = (bool(*)())dlsym(dylibDiscriptor, "init");
    assert(pluginInit != nullptr);
    pluginInit();
}

#else

void PluginsLoader::loadPlugins(std::string pathString, bool withManualRegister) {
    for (auto& p : directory_iterator(pathString)) {
        path dllPath = p.path();
        if (dllPath.extension() == ".dll") {
            HMODULE desc = LoadLibrary(dllPath.generic_string().c_str());

            if (!desc) {
                Logger::getInstance().log(dllPath.generic_string() + " library loading error");
                continue;
            }
        }
    }
}

#endif
