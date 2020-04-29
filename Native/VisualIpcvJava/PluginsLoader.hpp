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
    static PluginsLoader& getInstance() {
        static PluginsLoader instance;
        return instance;
    }
    
    void loadPlugins(std::string pathString, bool withManualRegister);
private:
    PluginsLoader() {}
    PluginsLoader(const PluginsLoader&);
    PluginsLoader& operator=(PluginsLoader&);
    
    void manualRegister(void* dylibDiscriptor);
};

#endif /* PluginsLoader_hpp */
