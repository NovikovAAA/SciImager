//
//  Vector2Brake.cpp
//  BaseProcessors
//
//  Created by Артём Новиков on 12.06.2020.
//  Copyright © 2020 Артём Новиков. All rights reserved.
//

#include "Vector2Brake.hpp"
#include "ProcessorManager.hpp"

#include <vector>

using namespace::std;

bool vector2LoadResult = ProcessorManager::registerProcessor(new Vector2Brake());

Vector2Brake::Vector2Brake() : Processor("Vector2Brake", "Core", "C++ Base",
{ProcessorProperty("Vector", BaseDataType(BaseDataTypeClassifier::VECTOR2))},
{ProcessorProperty("X", BaseDataType(BaseDataTypeClassifier::DOUBLE)),
 ProcessorProperty("Y", BaseDataType(BaseDataTypeClassifier::DOUBLE))}) {}

DataBundle Vector2Brake::execute(const DataBundle &dataMap, DataBundle &nodeSate) {
    vector<double> vector2 = dataMap.read<vector<double>>("Vector");
    DataBundle resultDataBundle;
    resultDataBundle.write("X", vector2[0]);
    resultDataBundle.write("Y", vector2[1]);
    prepareResult(&resultDataBundle);
    return resultDataBundle;
}
