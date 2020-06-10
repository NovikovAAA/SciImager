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
    Mat *image = dataMap.read<Mat*>("Value");
    Mat *coppiedImage = new Mat();
    image->copyTo(*coppiedImage);
    
    DataBundle resultDataBundle;
    resultDataBundle.write("Result", coppiedImage);
    prepareResult(&resultDataBundle);
    return resultDataBundle;
}
