//
//  DoublePropertyProcessor.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 07.06.2020.
//

#include "DoublePropertyProcessor.hpp"
#include "ProcessorManager.hpp"

bool doublePropertyProcessorResult = ProcessorManager::registerProcessor(new DoublePropertyProcessor());

DoublePropertyProcessor::DoublePropertyProcessor() : Processor("DoubleProperty", "Core", "TEST_C++",
{ProcessorProperty("Value", BaseDataType(BaseDataTypeClassifier::DOUBLE))},
{ProcessorProperty("Result", BaseDataType(BaseDataTypeClassifier::DOUBLE))}, true) {}

DataBundle DoublePropertyProcessor::execute(const DataBundle &dataMap, DataBundle &nodeSate) {
    double inputValue = dataMap.read<double>("Value");
    
    DataBundle resultDataBundle;
    resultDataBundle.write("Result", inputValue);
    prepareResult(&resultDataBundle);
    return resultDataBundle;
}
