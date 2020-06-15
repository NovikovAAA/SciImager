//
//  StringConcatProcessor.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 20.04.2020.
//

#include "StringConcatProcessor.hpp"
#include "ProcessorManager.hpp"

bool stringConcatLoadResult = ProcessorManager::registerProcessor(new StringConcatProcessor());

StringConcatProcessor::StringConcatProcessor() : Processor("StringConcat", "Core", "C++ Base",
{ProcessorProperty("firstString", BaseDataType(BaseDataTypeClassifier::STRING, {0, 0, 0, 0})),
 ProcessorProperty("secondString", BaseDataType(BaseDataTypeClassifier::STRING, {0, 0, 0, 0}))},
{ProcessorProperty("result", BaseDataType(BaseDataTypeClassifier::STRING, {0, 0, 0, 0}))}) {}

DataBundle StringConcatProcessor::execute(const DataBundle &dataMap, DataBundle &nodeSate) {
    std::string firstString = dataMap.read<std::string>(inputProperties[0].name);
    std::string secondString = dataMap.read<std::string>(inputProperties[1].name);
    
    std::string result = concat(firstString, secondString);
    return executionResult(outputProperties[0].name, result);
}

std::string StringConcatProcessor::concat(std::string firstString, std::string secondString) {
    return firstString + secondString;
}
