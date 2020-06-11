//
//  CarNumberFill.cpp
//  ImageProcessors
//
//  Created by Артём Новиков on 12.06.2020.
//  Copyright © 2020 Артём Новиков. All rights reserved.
//

#include "CarNumberFill.hpp"
#include "ProcessorManager.hpp"
#include "BaseCarDetector.hpp"

#include <opencv2/imgproc.hpp>
#include <opencv2/objdetect/objdetect.hpp>
#include <filesystem>

using namespace std::filesystem;

bool carNumbersFillProccessorLoadResult = ProcessorManager::registerProcessor(new CarNumberFill());

CarNumberFill::CarNumberFill() : Processor("CarNumbersFiller", "Core", "C++ Image Processors",
{ProcessorProperty("image", BaseDataType(BaseDataTypeClassifier::IMAGE))},
{ProcessorProperty("result", BaseDataType(BaseDataTypeClassifier::IMAGE))}) {}

DataBundle CarNumberFill::execute(const DataBundle &dataMap, DataBundle &nodeSate) {
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
    BaseCarDetector *detector = new BaseCarDetector();
    vector<Rect> numbersRects = detector->obtainNumbersRects(image);
    
    for (int i = 0; i < numbersRects.size(); i++) {
        rectangle(*image, numbersRects[i], Scalar(0, 0, 0), -1);
    }
    
    DataBundle resultDataBundle;
    resultDataBundle.write("result", new Mat(*image));
    prepareResult(&resultDataBundle);
    return resultDataBundle;
}
