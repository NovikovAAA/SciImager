//
//  Vector3Brake.cpp
//  BaseProcessors
//
//  Created by Артём Новиков on 12.06.2020.
//  Copyright © 2020 Артём Новиков. All rights reserved.
//

#include "Vector3Break.hpp"
#include "ProcessorManager.hpp"

bool vector3LoadResult = ProcessorManager::registerProcessor(new Vector3Break());

Vector3Break::Vector3Break() : VectorBreakBase("Vector3Break", "Core", "C++ Base",
{ProcessorProperty("Vector", BaseDataType(BaseDataTypeClassifier::VECTOR3))},
{ProcessorProperty("X", BaseDataType(BaseDataTypeClassifier::DOUBLE)),
 ProcessorProperty("Y", BaseDataType(BaseDataTypeClassifier::DOUBLE)),
 ProcessorProperty("Z", BaseDataType(BaseDataTypeClassifier::DOUBLE))}) {}
