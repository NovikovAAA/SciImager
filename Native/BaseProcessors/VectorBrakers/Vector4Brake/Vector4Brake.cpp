//
//  Vector4Brake.cpp
//  BaseProcessors
//
//  Created by Артём Новиков on 12.06.2020.
//  Copyright © 2020 Артём Новиков. All rights reserved.
//

#include "Vector4Brake.hpp"
#include "ProcessorManager.hpp"

bool vector4LoadResult = ProcessorManager::registerProcessor(new Vector4Brake());

Vector4Brake::Vector4Brake() : VectorBrakeBase("Vector4Brake", "Core", "C++ Base",
{ProcessorProperty("Vector", BaseDataType(BaseDataTypeClassifier::VECTOR4))},
{ProcessorProperty("X", BaseDataType(BaseDataTypeClassifier::DOUBLE)),
 ProcessorProperty("Y", BaseDataType(BaseDataTypeClassifier::DOUBLE)),
 ProcessorProperty("Z", BaseDataType(BaseDataTypeClassifier::DOUBLE)),
 ProcessorProperty("W", BaseDataType(BaseDataTypeClassifier::DOUBLE))}) {}
