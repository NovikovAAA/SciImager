//
//  BlurProcessor.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 13.06.2020.
//

#include "AverageBlur.hpp"
#include "ProcessorManager.hpp"

#include <opencv2/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>

#include "Logger.hpp"

using namespace cv;

bool blurProcessorProccessorLoadResult = ProcessorManager::registerProcessor(new AverageBlur());

AverageBlur::AverageBlur() : Processor("AverageBlur", "Core", "C++ Image Processors",
{ProcessorProperty("image", BaseDataType(BaseDataTypeClassifier::IMAGE)),
 ProcessorProperty("width", BaseDataType(BaseDataTypeClassifier::INTEGER)),
 ProcessorProperty("height", BaseDataType(BaseDataTypeClassifier::INTEGER))},
{ProcessorProperty("result", BaseDataType(BaseDataTypeClassifier::IMAGE))}) {}

DataBundle AverageBlur::execute(const DataBundle &dataMap, DataBundle &nodeSate) {
    Mat* image;
    try {
        image = dataMap.read<Mat *>(inputProperties[0].name);
    } catch (const std::exception& e) {
        return executionResult(outputProperties[0].name, new Mat());
    }
    
    int width = dataMap.read<int>(inputProperties[1].name);
    int height = dataMap.read<int>(inputProperties[2].name);
    
    width = width > 0 ? width : 1;
    height = height > 0 ? height : 1;
    
    Mat *dstImage = new Mat();
    blur(*image, *dstImage, Size(width, height));
    return executionResult(outputProperties[0].name, dstImage);
}
