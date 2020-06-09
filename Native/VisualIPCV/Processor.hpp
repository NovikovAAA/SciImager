//
//  Processor.hpp
//  VisualIPCV
//
//  Created by Артём Новиков on 15.02.2020.
//

#ifndef Processor_hpp
#define Processor_hpp

#include <stdio.h>
#include <vector>
#include "DataBundle.hpp"
#include "ProcessorProperty.hpp"

class Processor {
public:
    IPCV_API Processor(const Processor & object);
    IPCV_API Processor(std::string name, std::string module, std::string category, std::vector<ProcessorProperty> inputProperties, std::vector<ProcessorProperty> outputProperties, bool isProperty = false);
    
    std::string name;
    std::string module;
    std::string category;
    
    std::vector<ProcessorProperty> inputProperties;
    std::vector<ProcessorProperty> outputProperties;
    
    bool isProperty;
    
    IPCV_API virtual DataBundle execute(DataBundle const &dataMap, DataBundle &nodeSate) = 0;
    IPCV_API virtual void preExecute(DataBundle nodeState) {}
    IPCV_API virtual void postExecute(DataBundle nodeState) {}
    IPCV_API virtual void onCreate(DataBundle nodeState) {}
    IPCV_API virtual void onDestroyed(DataBundle nodeState) {}
    
    IPCV_API void prepareResult(DataBundle *resultDataBundle);
};

#endif /* Processor_hpp */

