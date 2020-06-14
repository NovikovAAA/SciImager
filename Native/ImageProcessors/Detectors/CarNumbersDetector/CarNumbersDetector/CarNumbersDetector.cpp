//
//  CarNumbersDetector.cpp
//  ImageProcessors
//
//  Created by Артём Новиков on 11.06.2020.
//  Copyright © 2020 Артём Новиков. All rights reserved.
//

#include "CarNumbersDetector.hpp"
#include "ProcessorManager.hpp"
#include "BaseCarDetector.hpp"

#include <filesystem>

using namespace std::filesystem;

bool carNumbersDetectorProccessorLoadResult = ProcessorManager::registerProcessor(new CarNumbersDetector());

CarNumbersDetector::CarNumbersDetector() : Processor("CarNumbersDetector", "Core", "C++ Image Processors",
{ProcessorProperty("image", BaseDataType(BaseDataTypeClassifier::IMAGE)),
 ProcessorProperty("Rectangle Width", BaseDataType(BaseDataTypeClassifier::DOUBLE)),
 ProcessorProperty("Selection Color", BaseDataType(BaseDataTypeClassifier::VECTOR3))},
{ProcessorProperty("result", BaseDataType(BaseDataTypeClassifier::IMAGE))}) {}

DataBundle CarNumbersDetector::execute(const DataBundle &dataMap, DataBundle &nodeSate) {
    Mat* image;
    try {
        image = dataMap.read<Mat *>(inputProperties[0].name);
    } catch (const std::exception& e) {
        return executionResult(outputProperties[0].name, new Mat());
    }
    
    double rectangleWidth = dataMap.read<double>(inputProperties[1].name);
    vector<double> colorVector = dataMap.read<vector<double>>(inputProperties[2].name);
 
    BaseCarDetector *detector = new BaseCarDetector();
    auto detectResult = detector->obtainImageWithSelectedNumbers(image, colorVector, rectangleWidth);
    return executionResult(outputProperties[0].name, detectResult);
}
