//
//  MulProcessor.cpp
//  BaseProcessors
//
//  Created by Артём Новиков on 12.06.2020.
//  Copyright © 2020 Артём Новиков. All rights reserved.
//

#include "MulProcessor.hpp"
#include "ProcessorManager.hpp"

bool mulLoadResult = ProcessorManager::registerProcessor(new MulProcessor());

MulProcessor::MulProcessor() : Processor("MulProcessor", "Core", "C++ Base",
{ProcessorProperty("a", BaseDataType(BaseDataTypeClassifier::DOUBLE)),
 ProcessorProperty("b", BaseDataType(BaseDataTypeClassifier::DOUBLE))},
{ProcessorProperty("result", BaseDataType(BaseDataTypeClassifier::DOUBLE))}) {}

DataBundle MulProcessor::execute(const DataBundle &dataMap, DataBundle &nodeSate) {
    double a = dataMap.read<double>(inputProperties[0].name);
    double b = dataMap.read<double>(inputProperties[1].name);
    
    double result = mul(a, b);
    return executionResult(outputProperties[0].name, result);
}

double MulProcessor::mul(double a, double b) {
    return a * b;
}
