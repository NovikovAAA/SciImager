//
//  Vector3Brake.cpp
//  BaseProcessors
//
//  Created by Артём Новиков on 12.06.2020.
//  Copyright © 2020 Артём Новиков. All rights reserved.
//

#include "Vector3Brake.hpp"
#include "ProcessorManager.hpp"

#include <vector>

using namespace::std;

bool vector3LoadResult = ProcessorManager::registerProcessor(new Vector3Brake());

Vector3Brake::Vector3Brake() : Processor("Vector3Brake", "Core", "C++ Base",
{ProcessorProperty("Vector", BaseDataType(BaseDataTypeClassifier::VECTOR3))},
{ProcessorProperty("X", BaseDataType(BaseDataTypeClassifier::DOUBLE)),
 ProcessorProperty("Y", BaseDataType(BaseDataTypeClassifier::DOUBLE)),
 ProcessorProperty("Z", BaseDataType(BaseDataTypeClassifier::DOUBLE))}) {}

DataBundle Vector3Brake::execute(const DataBundle &dataMap, DataBundle &nodeSate) {
    vector<double> vector3 = dataMap.read<vector<double>>("Vector");
    DataBundle resultDataBundle;
    resultDataBundle.write("X", vector3[0]);
    resultDataBundle.write("Y", vector3[1]);
    resultDataBundle.write("Z", vector3[2]);
    prepareResult(&resultDataBundle);
    return resultDataBundle;
}
