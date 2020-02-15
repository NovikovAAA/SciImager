//
//  SumProcessor.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 15.02.2020.
//

#include "SumProcessor.hpp"
#include "ProcessorManager.hpp"

bool _ = ProcessorManager::registerProcessor(new SumProcessor());

SumProcessor::SumProcessor() : Processor("Sum", "Core", "Math",
{ProcessorProperty("a", DataTypeJ()), ProcessorProperty("b", DataTypeJ())},
{ProcessorProperty("result", DataTypeJ())}) {}

DataBundle SumProcessor::execute(const DataBundle &dataMap, DataBundle &nodeSate) {
    double a = dataMap.read<double>("a");
    double b = dataMap.read<double>("b");
    
    double result = sum(a, b);
    
    DataBundle resultDataBundle;
    resultDataBundle.write("result", result);
    return resultDataBundle;
}

double SumProcessor::sum(double a, double b) {
    return a + b;
}


