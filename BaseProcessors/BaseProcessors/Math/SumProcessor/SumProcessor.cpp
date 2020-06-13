//
//  SumProcessor.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 15.02.2020.
//

#include "SumProcessor.hpp"
#include "ProcessorManager.hpp"

bool sumLoadResult = ProcessorManager::registerProcessor(new SumProcessor());

SumProcessor::SumProcessor() : Processor("SumProcessor", "Core", "C++ Base",
{ProcessorProperty("a", BaseDataType(BaseDataTypeClassifier::DOUBLE)),
 ProcessorProperty("b", BaseDataType(BaseDataTypeClassifier::DOUBLE))},
{ProcessorProperty("result", BaseDataType(BaseDataTypeClassifier::DOUBLE))}) {}

DataBundle SumProcessor::execute(const DataBundle &dataMap, DataBundle &nodeSate) {
    double a = dataMap.read<double>(inputProperties[0].name);
    double b = dataMap.read<double>(inputProperties[1].name);
    
    double result = sum(a, b);
    return executionResult(outputProperties[0].name, result);
}

double SumProcessor::sum(double a, double b) {
    return a + b;
}


