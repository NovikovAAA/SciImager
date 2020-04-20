//
//  StringConcatProcessor.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 20.04.2020.
//

#include "StringConcatProcessor.hpp"
#include "ProcessorManager.hpp"

bool stringConcatLoadResult = ProcessorManager::registerProcessor(new StringConcatProcessor());

StringConcatProcessor::StringConcatProcessor() : Processor("StringConcat", "Core", "TEST_C++",
{ProcessorProperty("firstString", BaseDataType(STRING, {0, 0, 0, 0})), ProcessorProperty("secondString", BaseDataType(STRING, {0, 0, 0, 0}))},
{ProcessorProperty("result", BaseDataType(STRING, {0, 0, 0, 0}))}) {}

DataBundle StringConcatProcessor::execute(const DataBundle &dataMap, DataBundle &nodeSate) {
    std::string firstString = dataMap.read<std::string>("firstString");
    std::string secondString = dataMap.read<std::string>("secondString");
    
    std::string result = concat(firstString, secondString);
    
    DataBundle resultDataBundle;
    resultDataBundle.write("result", result);
    prepareResult(&resultDataBundle);
    return resultDataBundle;
}

std::string StringConcatProcessor::concat(std::string firstString, std::string secondString) {
    return firstString + secondString;
}
