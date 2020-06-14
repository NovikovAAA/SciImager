//
//  CoincidenceChecker.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 15.06.2020.
//

#include "CoincidenceChecker.hpp"
#include "ProcessorManager.hpp"
#include <math.h>

bool coincidenceCheckerResult = ProcessorManager::registerProcessor(new CoincidenceChecker());

CoincidenceChecker::CoincidenceChecker() : Processor("CoincidenceChecker", "Core", "C++ Base",
{ProcessorProperty("value 1", BaseDataType(BaseDataTypeClassifier::DOUBLE)),
 ProcessorProperty("value 2", BaseDataType(BaseDataTypeClassifier::DOUBLE)),
 ProcessorProperty("accuracy", BaseDataType(BaseDataTypeClassifier::DOUBLE))},
{ProcessorProperty("result", BaseDataType(BaseDataTypeClassifier::STRING)),
 ProcessorProperty("discrepancy", BaseDataType(BaseDataTypeClassifier::DOUBLE))}) {}

DataBundle CoincidenceChecker::execute(const DataBundle &dataMap, DataBundle &nodeSate) {
    double value1 = dataMap.read<double>(inputProperties[0].name);
    double value2 = dataMap.read<double>(inputProperties[1].name);
    double accuracy = dataMap.read<double>(inputProperties[2].name);
    accuracy = accuracy > 0 ? accuracy : 0.01;
    
    double discrepancy = fabs(value1 - value2);
    string result = discrepancy > accuracy ? "don't match" : "match";
    
    return executionResult(ResultTransferModel<string>{ outputProperties[0].name, result },
                           ResultTransferModel<double>{ outputProperties[1].name, discrepancy });
}
