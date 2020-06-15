//
//  FaceDetectProcessor.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 03.05.2020.
//

#include "FaceDetectProcessor.hpp"
#include "ProcessorManager.hpp"
#include "BaseFaceDetector.hpp"

bool faceDetectProccessorLoadResult = ProcessorManager::registerProcessor(new FaceDetectProcessor());

FaceDetectProcessor::FaceDetectProcessor() : Processor("FaceDetect", "Core", "C++ Image Processors",
{ProcessorProperty("image", BaseDataType(BaseDataTypeClassifier::IMAGE)), ProcessorProperty("cascadePath", BaseDataType(BaseDataTypeClassifier::STRING))},
{ProcessorProperty("result", BaseDataType(BaseDataTypeClassifier::IMAGE))}) {}

DataBundle FaceDetectProcessor::execute(const DataBundle &dataMap, DataBundle &nodeSate) {
    Mat* image;
    try {
        image = dataMap.read<Mat *>(inputProperties[0].name);
    } catch (const std::exception& e) {
        return executionResult(outputProperties[0].name, new Mat());
    }
    
    std::string cascadePath = dataMap.read<std::string>(inputProperties[1].name);
    
    BaseFaceDetector *detector = new BaseFaceDetector(cascadePath);
    auto detectResult = detector->obtainImageWithSelectedFaces(image);
    return executionResult(outputProperties[0].name, detectResult);
}
