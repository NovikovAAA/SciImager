//
//  ImagePropertyProcessor.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 07.06.2020.
//

#include "ImagePropertyProcessor.hpp"
#include "ProcessorManager.hpp"
#include <opencv2/core.hpp>

using namespace cv;

bool imagePropertyProcessorResult = ProcessorManager::registerProcessor(new ImagePropertyProcessor());

ImagePropertyProcessor::ImagePropertyProcessor() : Processor("ImageProperty", "Core", "TEST_C++",
{ProcessorProperty("Value", BaseDataType(BaseDataTypeClassifier::IMAGE))},
{ProcessorProperty("Result", BaseDataType(BaseDataTypeClassifier::IMAGE))}, true) {}

DataBundle ImagePropertyProcessor::execute(const DataBundle &dataMap, DataBundle &nodeSate) {
    Mat *image = dataMap.read<Mat*>("Value");
    Mat* coppiedImage = new Mat();
    image->copyTo(*coppiedImage);
    
    DataBundle resultDataBundle;
    resultDataBundle.write("Result", coppiedImage);
    prepareResult(&resultDataBundle);
    return resultDataBundle;
}
