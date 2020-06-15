//
//  Vector2Brake.cpp
//  BaseProcessors
//
//  Created by Артём Новиков on 12.06.2020.
//  Copyright © 2020 Артём Новиков. All rights reserved.
//

#include "Vector2Break.hpp"
#include "ProcessorManager.hpp"

bool vector2LoadResult = ProcessorManager::registerProcessor(new Vector2Break());

Vector2Break::Vector2Break() : VectorBrakeBase("Vector2Break", "Core", "C++ Base",
{ProcessorProperty("Vector", BaseDataType(BaseDataTypeClassifier::VECTOR2))},
{ProcessorProperty("X", BaseDataType(BaseDataTypeClassifier::DOUBLE)),
 ProcessorProperty("Y", BaseDataType(BaseDataTypeClassifier::DOUBLE))}) {}
