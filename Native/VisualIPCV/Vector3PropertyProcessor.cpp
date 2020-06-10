//
//  Vector3PropertyProcessor.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 10.06.2020.
//

#include "Vector3PropertyProcessor.hpp"
#include "ProcessorManager.hpp"

bool vector3PropertyProcessorResult = ProcessorManager::registerProcessor(new Vector3PropertyProcessor());

Vector3PropertyProcessor::Vector3PropertyProcessor() : VectorBaseProcessor("Vector3Property", "Core", "TEST_C++",
{ProcessorProperty("Value", BaseDataType(BaseDataTypeClassifier::VECTOR3))},
{ProcessorProperty("Result", BaseDataType(BaseDataTypeClassifier::VECTOR3))}) {}
