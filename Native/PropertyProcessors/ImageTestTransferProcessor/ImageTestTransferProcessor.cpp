//
//  ImagePropertyProcessor.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 07.06.2020.
//

#include "ImageTestTransferProcessor.hpp"
#include "ProcessorManager.hpp"
#include <opencv2/core.hpp>

using namespace cv;

bool imageTestTransferProcessorResult = ProcessorManager::registerProcessor(new ImageTestTransferProcessor());

ImageTestTransferProcessor::ImageTestTransferProcessor() : Processor("ImageTestTransfer", "Core", "C++ Properties",
{ProcessorProperty("Value", BaseDataType(BaseDataTypeClassifier::IMAGE))},
{ProcessorProperty("Result", BaseDataType(BaseDataTypeClassifier::IMAGE))}, true) {}

DataBundle ImageTestTransferProcessor::execute(const DataBundle &dataMap, DataBundle &nodeSate) {
    Mat *image;
    try {
        image = dataMap.read<Mat*>(inputProperties[0].name);
    } catch (const std::exception& e) {
        return executionResult(outputProperties[0].name, new Mat());
    }
    return executionResult(outputProperties[0].name, new Mat(*image));
}
