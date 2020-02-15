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
    Processor(const Processor & object);
    Processor(std::string name, std::string module, std::string category, std::vector<ProcessorProperty> inputProperties, std::vector<ProcessorProperty> outputProperties);
    
    std::string name;
    std::string module;
    std::string category;
    
    std::vector<ProcessorProperty> inputProperties;
    std::vector<ProcessorProperty> outputProperties;
    
    virtual DataBundle execute(DataBundle const &dataMap, DataBundle &nodeSate) = 0;
    virtual void preExecute(DataBundle nodeState) {}
    virtual void postExecute(DataBundle nodeState) {}
    virtual void onCreate(DataBundle nodeState) {}
    virtual void onDestroyed(DataBundle nodeState) {}
};

#endif /* Processor_hpp */

