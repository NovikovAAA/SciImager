//
//  ImageCropProcessor.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 09.05.2020.
//

#include "ImageCropProcessor.hpp"
#include "ProcessorManager.hpp"
#include <opencv2/core.hpp>
#include <opencv2/imgproc.hpp>
#include <cassert>
#include "Logger.hpp"

using namespace cv;

bool ImageCropProcessorLoadResult = ProcessorManager::registerProcessor(new ImageCropProcessor());

ImageCropProcessor::ImageCropProcessor() : Processor("ImageCrop", "Core", "C++ Image Processors",
{ProcessorProperty("image", BaseDataType(BaseDataTypeClassifier::IMAGE)),
 ProcessorProperty("x", BaseDataType(BaseDataTypeClassifier::DOUBLE)),
 ProcessorProperty("y", BaseDataType(BaseDataTypeClassifier::DOUBLE)),
 ProcessorProperty("width", BaseDataType(BaseDataTypeClassifier::DOUBLE)),
 ProcessorProperty("height", BaseDataType(BaseDataTypeClassifier::DOUBLE))},
{ProcessorProperty("result", BaseDataType(BaseDataTypeClassifier::IMAGE))}) {}

DataBundle ImageCropProcessor::execute(const DataBundle &dataMap, DataBundle &nodeSate) {
    Mat* image;
    try {
        image = dataMap.read<Mat *>(inputProperties[0].name);
    } catch (const std::exception& e) {
        return executionResult(outputProperties[0].name, new Mat());
    }
    
    double x = dataMap.read<double>(inputProperties[1].name);
    double y = dataMap.read<double>(inputProperties[2].name);
    double width = dataMap.read<double>(inputProperties[3].name);
    double height = dataMap.read<double>(inputProperties[4].name);

    Mat sourceImage = *image;
    Size imageSize = sourceImage.size();

    x = x > 0 ? x : 1.0;
    y = y > 0 ? y : 1.0;
    width = width > 0 ? width : 1.0;
    height = height > 0 ? height : 1.0;

    width = x + width > imageSize.width ? width = imageSize.width - x : width;
    height = y + height > imageSize.height ? height = imageSize.height - y : height;

    Rect cropRect(x, y, width, height);
    Mat *croppedImage = new Mat(sourceImage(cropRect));
    return executionResult(outputProperties[0].name, croppedImage);
}
