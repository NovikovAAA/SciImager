//
//  Vector4PropertyProcessor.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 10.06.2020.
//

#include "Vector4PropertyProcessor.hpp"
#include "ProcessorManager.hpp"

bool vector4PropertyProcessorResult = ProcessorManager::registerProcessor(new Vector4PropertyProcessor());

Vector4PropertyProcessor::Vector4PropertyProcessor() : VectorBaseProcessor("Vector4Property", "Core", "TEST_C++",
{ProcessorProperty("Value", BaseDataType(BaseDataTypeClassifier::VECTOR4))},
{ProcessorProperty("Result", BaseDataType(BaseDataTypeClassifier::VECTOR4))}) {}
