//
//  Vector4Brake.cpp
//  BaseProcessors
//
//  Created by Артём Новиков on 12.06.2020.
//  Copyright © 2020 Артём Новиков. All rights reserved.
//

#include "Vector4Break.hpp"
#include "ProcessorManager.hpp"

bool vector4LoadResult = ProcessorManager::registerProcessor(new Vector4Break());

Vector4Break::Vector4Break() : VectorBreakBase("Vector4Break", "Core", "C++ Base",
{ProcessorProperty("Vector", BaseDataType(BaseDataTypeClassifier::VECTOR4))},
{ProcessorProperty("X", BaseDataType(BaseDataTypeClassifier::DOUBLE)),
 ProcessorProperty("Y", BaseDataType(BaseDataTypeClassifier::DOUBLE)),
 ProcessorProperty("Z", BaseDataType(BaseDataTypeClassifier::DOUBLE)),
 ProcessorProperty("W", BaseDataType(BaseDataTypeClassifier::DOUBLE))}) {}
