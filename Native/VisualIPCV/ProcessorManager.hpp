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
    IPCV_API static std::map<std::string, std::map<std::string, Processor *>>& getModules();

    IPCV_API static bool registerProcessor(Processor * processor);
    IPCV_API static Processor* find(const std::string &moduleName, const std::string &processorName);
};

#endif /* ProcessorManager_hpp */
