//
//  Vector2PropertyProcessor.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 07.06.2020.
//

#include "Vector2PropertyProcessor.hpp"
#include "ProcessorManager.hpp"

bool vector2PropertyProcessorResult = ProcessorManager::registerProcessor(new Vector2PropertyProcessor());

Vector2PropertyProcessor::Vector2PropertyProcessor() : VectorBaseProcessor("Vector2Property", "Core", "C++ Properties",
{ProcessorProperty("Value", BaseDataType(BaseDataTypeClassifier::VECTOR2))},
{ProcessorProperty("Result", BaseDataType(BaseDataTypeClassifier::VECTOR2))}) {}
