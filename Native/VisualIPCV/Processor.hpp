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
#include "ProcessorProperty.hpp"

class Processor {
public:
    Processor();
    Processor(const Processor & object);
    Processor(std::string name, std::string module, std::string category, std::vector<ProcessorProperty> inputProperties, std::vector<ProcessorProperty> outputProperties);
    
    std::string name;
    std::string module;
    std::string category;
    
    std::vector<ProcessorProperty> inputProperties;
    std::vector<ProcessorProperty> outputProperties;
};

#endif /* Processor_hpp */
