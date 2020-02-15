//
//  Processor.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 15.02.2020.
//

#include "Processor.hpp"

Processor::Processor(const Processor & object) : Processor(object.name, object.module, object.category, object.inputProperties, object.outputProperties) {}

Processor::Processor(std::string name, std::string module, std::string category, std::vector<ProcessorProperty> inputProperties, std::vector<ProcessorProperty> outputProperties) {
    this->name = name;
    this->module = module;
    this->category = category;
    
    for (int i = 0; i < inputProperties.size(); i++) {
        this->inputProperties.push_back(ProcessorProperty(inputProperties[i]));
    }
    
    for (int i = 0; i < outputProperties.size(); i++) {
        this->outputProperties.push_back(ProcessorProperty(outputProperties[i]));
    }
}
