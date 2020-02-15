//
//  Processor.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 15.02.2020.
//

#include "Processor.hpp"

Processor::Processor() {
    name = "";
    module = "";
    category = "";
    
    inputProperties = std::vector<ProcessorProperty>();
    outputProperties = std::vector<ProcessorProperty>();
}

Processor::Processor(const Processor & object) {
    this->name = object.name;
    this->module = object.module;
    this->category = object.category;
    
    for (int i = 0; i < object.inputProperties.size(); i++) {
        this->inputProperties.push_back(ProcessorProperty(object.inputProperties[i]));
    }
    
    for (int i = 0; i < object.outputProperties.size(); i++) {
        this->outputProperties.push_back(ProcessorProperty(object.outputProperties[i]));
    }
}

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
