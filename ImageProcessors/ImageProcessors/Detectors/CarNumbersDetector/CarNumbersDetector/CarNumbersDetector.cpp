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
        image = dataMap.read<Mat *>("image");
    } catch (const std::exception& e) {
        image = new Mat();

        DataBundle resultDataBundle;
        resultDataBundle.write("result", image);
        prepareResult(&resultDataBundle);
        return resultDataBundle;
    }
    vector<double> colorVector = dataMap.read<vector<double>>("Selection Color");
    double rectangleWidth = dataMap.read<double>("Rectangle Width");
 
    BaseCarDetector *detector = new BaseCarDetector();
    auto detectResult = detector->obtainImageWithSelectedNumbers(image, colorVector, rectangleWidth);
    
    DataBundle resultDataBundle;
    resultDataBundle.write("result", detectResult);
    prepareResult(&resultDataBundle);
    return resultDataBundle;
}
