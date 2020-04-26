//
//  ProcessorManager.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 15.02.2020.
//

#include "ProcessorManager.hpp"

std::map<std::string, std::map<std::string, Processor *>>& ProcessorManager::getModules() {
    static std::map<std::string, std::map<std::string, Processor *>> modules;
    return modules;
}

bool ProcessorManager::registerProcessor(Processor *processor) {
    getModules()[processor->module][processor->name] = processor;
    return true;
}

Processor* ProcessorManager::find(const std::string &moduleName, const std::string &processorName) {
    return getModules()[moduleName][processorName];
}
