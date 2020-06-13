//
//  MedianBlur.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 13.06.2020.
//

#include "MedianBlurProcessor.hpp"
#include "ProcessorManager.hpp"

#include <opencv2/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>

using namespace cv;

bool medianBlurProcessorProccessorLoadResult = ProcessorManager::registerProcessor(new MedianBlurProcessor());

MedianBlurProcessor::MedianBlurProcessor() : Processor("MedianBlur", "Core", "C++ Image Processors",
{ProcessorProperty("image", BaseDataType(BaseDataTypeClassifier::IMAGE)),
 ProcessorProperty("ksize", BaseDataType(BaseDataTypeClassifier::INTEGER))},
{ProcessorProperty("result", BaseDataType(BaseDataTypeClassifier::IMAGE))}) {}

DataBundle MedianBlurProcessor::execute(const DataBundle &dataMap, DataBundle &nodeSate) {
    Mat* image;
    try {
        image = dataMap.read<Mat *>(inputProperties[0].name);
    } catch (const std::exception& e) {
        return executionResult(outputProperties[0].name, new Mat());
    }
    
    int kSize = dataMap.read<int>(inputProperties[1].name);
    kSize = kSize > 2 ? kSize : 3;
    kSize = kSize % 2 == 0 ? kSize - 1 : kSize;
    
    Mat *dstImage = new Mat();
    medianBlur(*image, *dstImage, kSize);
    return executionResult(outputProperties[0].name, dstImage);
}
