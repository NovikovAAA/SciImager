//
//  ImageResize.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 13.06.2020.
//

#include "ImageResize.hpp"
#include "ProcessorManager.hpp"

#include <opencv2/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>

using namespace cv;

bool imageResizeLoadResult = ProcessorManager::registerProcessor(new ImageResize());

ImageResize::ImageResize() : Processor("ResizeProcessor", "Core", "C++ Image Processors",
{ProcessorProperty("image", BaseDataType(BaseDataTypeClassifier::IMAGE)),
 ProcessorProperty("width", BaseDataType(BaseDataTypeClassifier::DOUBLE)),
 ProcessorProperty("height", BaseDataType(BaseDataTypeClassifier::DOUBLE)),
 ProcessorProperty("interpolation type", BaseDataType(BaseDataTypeClassifier::INTEGER))},
{ProcessorProperty("result", BaseDataType(BaseDataTypeClassifier::IMAGE))}) {}

DataBundle ImageResize::execute(const DataBundle &dataMap, DataBundle &nodeSate) {
    Mat* image;
    try {
        image = dataMap.read<Mat *>(inputProperties[0].name);
    } catch (const std::exception& e) {
        return executionResult(outputProperties[0].name, new Mat());
    }
    double width = dataMap.read<double>(inputProperties[1].name);
    double height = dataMap.read<double>(inputProperties[2].name);
    int interpolationType = dataMap.read<int>(inputProperties[3].name);
    
    width = width <= 0 ? 1 : width > image->size().width ? image->size().width : width;
    height = height <= 0 ? 1 : height > image->size().height ? image->size().height : height;
    interpolationType = interpolationType < 0 || interpolationType == 6 || (interpolationType > 8 && interpolationType < 16) || interpolationType > 16 ? INTER_CUBIC : interpolationType;
    
    Mat *dstImage = new Mat();
    resize(*image, *dstImage, Size(width, height), interpolationType);
    
    return executionResult(outputProperties[0].name, dstImage);
}
