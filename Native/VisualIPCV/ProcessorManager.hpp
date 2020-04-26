//
//  ProcessorManager.hpp
//  VisualIPCV
//
//  Created by Артём Новиков on 15.02.2020.
//

#ifndef ProcessorManager_hpp
#define ProcessorManager_hpp

#include <stdio.h>
#include <map>
#include <string>
#include "Processor.hpp"

class ProcessorManager {
public:
    static std::map<std::string, std::map<std::string, Processor *>>& getModules();

    static bool registerProcessor(Processor * processor);
    static Processor* find(const std::string &moduleName, const std::string &processorName);
};

#endif /* ProcessorManager_hpp */
