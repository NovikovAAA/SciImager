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
    double a = dataMap.read<double>("a");
    double b = dataMap.read<double>("b");
    
    double result = mul(a, b);
    
    DataBundle resultDataBundle;
    resultDataBundle.write("result", result);
    prepareResult(&resultDataBundle);
    return resultDataBundle;
}

double MulProcessor::mul(double a, double b) {
    return a * b;
}
