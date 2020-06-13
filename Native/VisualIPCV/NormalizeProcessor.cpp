//
//  NormalizeProcessor.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 13.06.2020.
//

#include "NormalizeProcessor.hpp"
#include "ProcessorManager.hpp"

#include <opencv2/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>

using namespace cv;

bool NormalizeProcessorLoadResult = ProcessorManager::registerProcessor(new NormalizeProcessor());

NormalizeProcessor::NormalizeProcessor() : Processor("NormalizeMinMax", "Core", "C++ Image Processors",
{ProcessorProperty("image", BaseDataType(BaseDataTypeClassifier::IMAGE)),
 ProcessorProperty("alpha", BaseDataType(BaseDataTypeClassifier::DOUBLE)),
 ProcessorProperty("beta", BaseDataType(BaseDataTypeClassifier::DOUBLE))},
{ProcessorProperty("result", BaseDataType(BaseDataTypeClassifier::IMAGE))}) {}

DataBundle NormalizeProcessor::execute(const DataBundle &dataMap, DataBundle &nodeSate) {
    Mat* image;
    try {
        image = dataMap.read<Mat *>(inputProperties[0].name);
    } catch (const std::exception& e) {
        return executionResult(outputProperties[0].name, new Mat());
    }
    
    double alpha = dataMap.read<double>(inputProperties[1].name);
    double beta = dataMap.read<double>(inputProperties[2].name);
    
    Mat *dstImage = new Mat();
    try {
        cv::normalize(*image, *dstImage, alpha, beta, cv::NORM_MINMAX);
    } catch (const std::exception& e) {
        return executionResult(outputProperties[0].name, new Mat(*image));
    }
    return executionResult(outputProperties[0].name, dstImage);
}
