//
//  Vector2PropertyProcessor.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 07.06.2020.
//

#include "Vector2PropertyProcessor.hpp"
#include "ProcessorManager.hpp"

bool vector2PropertyProcessorResult = ProcessorManager::registerProcessor(new Vector2PropertyProcessor());

Vector2PropertyProcessor::Vector2PropertyProcessor() : Processor("Vector2Property", "Core", "TEST_C++",
{ProcessorProperty("Value", BaseDataType(BaseDataTypeClassifier::VECTOR2))},
{ProcessorProperty("Result", BaseDataType(BaseDataTypeClassifier::VECTOR2))}) {}

DataBundle Vector2PropertyProcessor::execute(const DataBundle &dataMap, DataBundle &nodeSate) {
    double *inputArray = dataMap.read<double *>("Value");
    
    double *copiedArray = new double(2);
    for (int i = 0; i < 2; i++) {
        copiedArray[i] = inputArray[i];
    }
    
    DataBundle resultDataBundle;
    resultDataBundle.write("Result", copiedArray);
    prepareResult(&resultDataBundle);
    return resultDataBundle;
}
