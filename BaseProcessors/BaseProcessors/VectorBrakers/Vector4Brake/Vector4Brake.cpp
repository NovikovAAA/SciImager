//
//  Vector4Brake.cpp
//  BaseProcessors
//
//  Created by Артём Новиков on 12.06.2020.
//  Copyright © 2020 Артём Новиков. All rights reserved.
//

#include "Vector4Brake.hpp"
#include "ProcessorManager.hpp"

#include <vector>

using namespace::std;

bool vector4LoadResult = ProcessorManager::registerProcessor(new Vector4Brake());

Vector4Brake::Vector4Brake() : Processor("Vector4Brake", "Core", "C++ Base",
{ProcessorProperty("Vector", BaseDataType(BaseDataTypeClassifier::VECTOR4))},
{ProcessorProperty("X", BaseDataType(BaseDataTypeClassifier::DOUBLE)),
 ProcessorProperty("Y", BaseDataType(BaseDataTypeClassifier::DOUBLE)),
 ProcessorProperty("Z", BaseDataType(BaseDataTypeClassifier::DOUBLE)),
 ProcessorProperty("W", BaseDataType(BaseDataTypeClassifier::DOUBLE))}) {}

DataBundle Vector4Brake::execute(const DataBundle &dataMap, DataBundle &nodeSate) {
    vector<double> vector4 = dataMap.read<vector<double>>("Vector");
    DataBundle resultDataBundle;
    resultDataBundle.write("X", vector4[0]);
    resultDataBundle.write("Y", vector4[1]);
    resultDataBundle.write("Z", vector4[2]);
    resultDataBundle.write("W", vector4[3]);
    prepareResult(&resultDataBundle);
    return resultDataBundle;
}
