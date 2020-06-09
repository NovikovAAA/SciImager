//
//  SumProcessor.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 15.02.2020.
//

#include "SumProcessor.hpp"
#include "ProcessorManager.hpp"

bool sumLoadResult = ProcessorManager::registerProcessor(new SumProcessor());

SumProcessor::SumProcessor() : Processor("SumC++", "Core", "TEST_C++",
{ProcessorProperty("a", BaseDataType(BaseDataTypeClassifier::DOUBLE)), ProcessorProperty("b", BaseDataType(BaseDataTypeClassifier::DOUBLE))},
{ProcessorProperty("result", BaseDataType(BaseDataTypeClassifier::DOUBLE))}) {}

DataBundle SumProcessor::execute(const DataBundle &dataMap, DataBundle &nodeSate) {
    double a = dataMap.read<double>("a");
    double b = dataMap.read<double>("b");
    
    double result = sum(a, b);
    
    DataBundle resultDataBundle;
    resultDataBundle.write("result", result);
    prepareResult(&resultDataBundle);
    return resultDataBundle;
}

double SumProcessor::sum(double a, double b) {
    return a + b;
}


