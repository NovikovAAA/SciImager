//
//  LibrariesLoader.cpp
//  VisualIpcvJava
//
//  Created by Артём Новиков on 29.04.2020.
//

#include "PluginsLoader.hpp"
#include <VisualIPCV/Logger.hpp>
#include <filesystem>
#include <dlfcn.h>

using namespace std::filesystem;

void PluginsLoader::loadPlugins(std::string pathString, bool withManualRegister) {
    for(auto& p: directory_iterator(pathString)) {
        path dylibPath = p.path();
        if (dylibPath.extension() == ".dylib" || dylibPath.extension() == ".dll") {
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

void PluginsLoader::manualRegister(void *dylibDiscriptor) {
    auto pluginInit = (bool(*)())dlsym(dylibDiscriptor, "init");
    assert(pluginInit != nullptr);
    pluginInit();
}
