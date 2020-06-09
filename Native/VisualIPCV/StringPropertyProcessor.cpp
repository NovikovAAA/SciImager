//
//  StringPropertyProcessor.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 10.06.2020.
//

#include "StringPropertyProcessor.hpp"
#include "ProcessorManager.hpp"
#include <string>

bool stringPropertyProcessorResult = ProcessorManager::registerProcessor(new StringPropertyProcessor());

StringPropertyProcessor::StringPropertyProcessor() : Processor("StringProperty", "Core", "TEST_C++",
{ProcessorProperty("Value", BaseDataType(BaseDataTypeClassifier::STRING))},
{ProcessorProperty("Result", BaseDataType(BaseDataTypeClassifier::STRING))}, true) {}

DataBundle StringPropertyProcessor::execute(const DataBundle &dataMap, DataBundle &nodeSate) {
    std::string inputValue = dataMap.read<std::string>("Value");
    
    DataBundle resultDataBundle;
    resultDataBundle.write("Result", inputValue);
    prepareResult(&resultDataBundle);
    return resultDataBundle;
}
