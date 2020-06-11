//
//  CarNumbersSaver.cpp
//  ImageProcessors
//
//  Created by Артём Новиков on 11.06.2020.
//  Copyright © 2020 Артём Новиков. All rights reserved.
//

#include "CarNumbersSaver.hpp"
#include "ProcessorManager.hpp"
#include "BaseCarDetector.hpp"
#include "ImageSaver.hpp"

bool carNumbersSaverProccessorLoadResult = ProcessorManager::registerProcessor(new CarNumbersSaver());

CarNumbersSaver::CarNumbersSaver() : Processor("CarNumbersSaver", "Core", "C++ Image Processors",
{ProcessorProperty("image", BaseDataType(BaseDataTypeClassifier::IMAGE)),
 ProcessorProperty("saving path", BaseDataType(BaseDataTypeClassifier::STRING))},
{ProcessorProperty("result", BaseDataType(BaseDataTypeClassifier::IMAGE))}) {}

DataBundle CarNumbersSaver::execute(const DataBundle &dataMap, DataBundle &nodeSate) {
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
    
    string savingPath = dataMap.read<string>("saving path");
    
    BaseCarDetector *detector = new BaseCarDetector();
    auto detectResult = detector->obtainImageWithSelectedNumbers(image);
    vector<Rect> numbersRects = detector->obtainNumbersRects(image);
    
    ImageSaver saver = ImageSaver(savingPath);
    for (int i = 0; i < numbersRects.size(); i++) {
        Mat imageToSave = *image;
        saver.save(imageToSave(numbersRects[i]), to_string(i), "png");
    }
    
    DataBundle resultDataBundle;
    resultDataBundle.write("result", detectResult);
    prepareResult(&resultDataBundle);
    return resultDataBundle;
}
