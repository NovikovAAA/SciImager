//
//  FaceDetectSaver.cpp
//  ImageProcessors
//
//  Created by Артём Новиков on 12.06.2020.
//  Copyright © 2020 Артём Новиков. All rights reserved.
//

#include "FaceSaver.hpp"
#include "ProcessorManager.hpp"
#include "BaseFaceDetector.hpp"
#include "ImageSaver.hpp"

bool faceSaverProccessorLoadResult = ProcessorManager::registerProcessor(new FaceSaver());

FaceSaver::FaceSaver() : Processor("FaceSaver", "Core", "C++ Image Processors",
{ProcessorProperty("image", BaseDataType(BaseDataTypeClassifier::IMAGE)),
 ProcessorProperty("saving path", BaseDataType(BaseDataTypeClassifier::STRING)),
 ProcessorProperty("cascadePath", BaseDataType(BaseDataTypeClassifier::STRING))},
{ProcessorProperty("result", BaseDataType(BaseDataTypeClassifier::IMAGE))}) {}

DataBundle FaceSaver::execute(const DataBundle &dataMap, DataBundle &nodeSate) {
    Mat* image;
    try {
        image = dataMap.read<Mat *>(inputProperties[0].name);
    } catch (const std::exception& e) {
        return executionResult(outputProperties[0].name, new Mat());
    }
    
    string savingPath = dataMap.read<string>(inputProperties[1].name);
    string cascadePath = dataMap.read<string>(inputProperties[2].name);
    
    BaseFaceDetector *detector = new BaseFaceDetector(cascadePath);
    auto detectResult = detector->obtainImageWithSelectedFaces(image);
    vector<Rect> facesRects = detector->obtainFacesRects(image);
    
    ImageSaver saver = ImageSaver(savingPath);
    for (int i = 0; i < facesRects.size(); i++) {
        Mat imageToSave = *image;
        saver.save(imageToSave(facesRects[i]), to_string(i), "png");
    }
    return executionResult(outputProperties[0].name, detectResult);
}
