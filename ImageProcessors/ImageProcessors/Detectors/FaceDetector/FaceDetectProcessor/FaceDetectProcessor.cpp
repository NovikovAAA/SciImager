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
        image = dataMap.read<Mat *>("image");
    } catch (const std::exception& e) {
        image = new Mat();

        DataBundle resultDataBundle;
        resultDataBundle.write("result", image);
        prepareResult(&resultDataBundle);
        return resultDataBundle;
    }
    
    std::string cascadePath = dataMap.read<std::string>("cascadePath");
    
    BaseFaceDetector *detector = new BaseFaceDetector(cascadePath);
    auto detectResult = detector->obtainImageWithSelectedFaces(image);
    
    DataBundle resultDataBundle;
    resultDataBundle.write("result", detectResult);
    prepareResult(&resultDataBundle);
    return resultDataBundle;
}
