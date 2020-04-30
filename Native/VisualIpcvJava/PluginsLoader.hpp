//
//  LibrariesLoader.hpp
//  VisualIpcvJava
//
//  Created by Артём Новиков on 29.04.2020.
//

#ifndef PluginsLoader_hpp
#define PluginsLoader_hpp

#include <stdio.h>
#include <string>

class PluginsLoader {
public:
    void loadPlugins(std::string pathString, bool withManualRegister);
private:
    void manualRegister(void* dylibDiscriptor);
};

#endif /* PluginsLoader_hpp */
