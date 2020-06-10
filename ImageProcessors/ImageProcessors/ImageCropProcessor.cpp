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
    Mat *image = dataMap.read<Mat*>("image");
    assert(image != nullptr);
    
    double x = dataMap.read<double>("x");
    double y = dataMap.read<double>("y");
    double width = dataMap.read<double>("width");
    double height = dataMap.read<double>("height");

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

    DataBundle resultDataBundle;
    resultDataBundle.write("result", croppedImage);
    prepareResult(&resultDataBundle);
    
    return resultDataBundle;
}
