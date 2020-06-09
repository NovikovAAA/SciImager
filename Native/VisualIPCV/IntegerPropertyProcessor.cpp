//
//  IntegerPropertyProcessor.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 07.06.2020.
//

#include "IntegerPropertyProcessor.hpp"
#include "ProcessorManager.hpp"

bool integerPropertyProcessorResult = ProcessorManager::registerProcessor(new IntegerPropertyProcessor());

IntegerPropertyProcessor::IntegerPropertyProcessor() : Processor("IntegerProperty", "Core", "TEST_C++",
{ProcessorProperty("Value", BaseDataType(BaseDataTypeClassifier::INTEGER))},
{ProcessorProperty("Result", BaseDataType(BaseDataTypeClassifier::INTEGER))}, true) {}

DataBundle IntegerPropertyProcessor::execute(const DataBundle &dataMap, DataBundle &nodeSate) {
    int inputValue = dataMap.read<int>("Value");
    
    DataBundle resultDataBundle;
    resultDataBundle.write("Result", inputValue);
    prepareResult(&resultDataBundle);
    return resultDataBundle;
}
