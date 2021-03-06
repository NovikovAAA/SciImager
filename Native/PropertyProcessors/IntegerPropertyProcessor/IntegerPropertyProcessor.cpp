//
//  IntegerPropertyProcessor.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 07.06.2020.
//

#include "IntegerPropertyProcessor.hpp"
#include "ProcessorManager.hpp"

bool integerPropertyProcessorResult = ProcessorManager::registerProcessor(new IntegerPropertyProcessor());

IntegerPropertyProcessor::IntegerPropertyProcessor() : Processor("IntegerProperty", "Core", "C++ Properties",
{ProcessorProperty("Value", BaseDataType(BaseDataTypeClassifier::INTEGER))},
{ProcessorProperty("Result", BaseDataType(BaseDataTypeClassifier::INTEGER))}, true) {}

DataBundle IntegerPropertyProcessor::execute(const DataBundle &dataMap, DataBundle &nodeSate) {
    int inputValue = dataMap.read<int>(inputProperties[0].name);
    return executionResult(outputProperties[0].name, inputValue);
}
