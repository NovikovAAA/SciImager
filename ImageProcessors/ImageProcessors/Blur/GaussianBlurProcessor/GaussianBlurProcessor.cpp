//
//  GaussianProcessor.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 13.06.2020.
//

#include "GaussianBlurProcessor.hpp"
#include "ProcessorManager.hpp"

#include <opencv2/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>

using namespace cv;

bool gaussianBlurProcessorProccessorLoadResult = ProcessorManager::registerProcessor(new GaussianBlurProcessor());

GaussianBlurProcessor::GaussianBlurProcessor() : Processor("GaussianBlur", "Core", "C++ Image Processors",
{ProcessorProperty("image", BaseDataType(BaseDataTypeClassifier::IMAGE)),
 ProcessorProperty("width", BaseDataType(BaseDataTypeClassifier::INTEGER)),
 ProcessorProperty("height", BaseDataType(BaseDataTypeClassifier::INTEGER)),
 ProcessorProperty("sigma X", BaseDataType(BaseDataTypeClassifier::DOUBLE)),
 ProcessorProperty("sigma Y", BaseDataType(BaseDataTypeClassifier::DOUBLE))},
{ProcessorProperty("result", BaseDataType(BaseDataTypeClassifier::IMAGE))}) {}

DataBundle GaussianBlurProcessor::execute(const DataBundle &dataMap, DataBundle &nodeSate) {
    Mat* image;
    try {
        image = dataMap.read<Mat *>(inputProperties[0].name);
    } catch (const std::exception& e) {
        return executionResult(outputProperties[0].name, new Mat());
    }
    
    int width = dataMap.read<int>(inputProperties[1].name);
    int height = dataMap.read<int>(inputProperties[2].name);
    
    double sigmaX = dataMap.read<double>(inputProperties[3].name);
    double sigmaY = dataMap.read<double>(inputProperties[4].name);
    
    width = width > 0 ? width : 1;
    height = height > 0 ? height : 1;
    
    width = width % 2 == 0 ? width - 1 : width;
    height = height % 2 == 0 ? height - 1 : height;
    
    Mat *dstImage = new Mat();
    GaussianBlur(*image, *dstImage, Size(width, height), sigmaX, sigmaY);
    return executionResult(outputProperties[0].name, dstImage);
}
