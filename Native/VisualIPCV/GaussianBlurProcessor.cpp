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
        image = dataMap.read<Mat *>("image");
    } catch (const std::exception& e) {
        image = new Mat();

        DataBundle resultDataBundle;
        resultDataBundle.write("result", image);
        prepareResult(&resultDataBundle);
        return resultDataBundle;
    }
    
    int width = dataMap.read<int>("width");
    int height = dataMap.read<int>("height");
    
    double sigmaX = dataMap.read<double>("sigma X");
    double sigmaY = dataMap.read<double>("sigma Y");
    
    width = width > 0 ? width : 1;
    height = height > 0 ? height : 1;
    
    width = width % 2 == 0 ? width - 1 : width;
    height = height % 2 == 0 ? height - 1 : height;
    
    Mat *dstImage = new Mat();
    GaussianBlur(*image, *dstImage, Size(width, height), sigmaX, sigmaY);
    
    DataBundle resultDataBundle;
    resultDataBundle.write("result", dstImage);
    prepareResult(&resultDataBundle);
    return resultDataBundle;
}
