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
        image = dataMap.read<Mat *>("image");
    } catch (const std::exception& e) {
        image = new Mat();

        DataBundle resultDataBundle;
        resultDataBundle.write("result", image);
        prepareResult(&resultDataBundle);
        return resultDataBundle;
    }
    
    int kSize = dataMap.read<int>("ksize");
    
    kSize = kSize > 2 ? kSize : 3;
    kSize = kSize % 2 == 0 ? kSize - 1 : kSize;
    
    Mat *dstImage = new Mat();
    medianBlur(*image, *dstImage, kSize);
    
    DataBundle resultDataBundle;
    resultDataBundle.write("result", dstImage);
    prepareResult(&resultDataBundle);
    return resultDataBundle;
}
