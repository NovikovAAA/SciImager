//
//  VectorBrakeBase.cpp
//  BaseProcessors
//
//  Created by Артём Новиков on 13.06.2020.
//  Copyright © 2020 Артём Новиков. All rights reserved.
//

#include "VectorBreakBase.hpp"
#include <vector>

using namespace::std;

VectorBreakBase::VectorBreakBase(std::string name, std::string module, std::string category, std::vector<ProcessorProperty> inputProperties, std::vector<ProcessorProperty> outputProperties) : Processor(name, module, category, inputProperties, outputProperties) {}

DataBundle VectorBreakBase::execute(const DataBundle &dataMap, DataBundle &nodeSate) {
    vector<double> vector4 = dataMap.read<vector<double>>(inputProperties[0].name);
    vector<ResultTransferModel<double>> resultModels;
    for (int i = 0; i < outputProperties.size() && i < vector4.size(); i++) {
        resultModels.push_back(ResultTransferModel<double>{ outputProperties[i].name, vector4[i] });
    }
    return executionResult(resultModels);
}
