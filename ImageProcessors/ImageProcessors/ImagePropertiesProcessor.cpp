//
//  ImageProperties.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 04.05.2020.
//

#include "ImagePropertiesProcessor.hpp"
#include "ProcessorManager.hpp"
#include <opencv2/core.hpp>
#include <opencv2/imgproc.hpp>

using namespace cv;

bool imagePropertiesProccessorLoadResult = ProcessorManager::registerProcessor(new ImagePropertiesProcessor());

ImagePropertiesProcessor::ImagePropertiesProcessor() : Processor("ImageProperties", "Core", "C++ Image Processors",
{ProcessorProperty("image", BaseDataType(BaseDataTypeClassifier::IMAGE))},
{ProcessorProperty("height", BaseDataType(BaseDataTypeClassifier::DOUBLE)),
 ProcessorProperty("width", BaseDataType(BaseDataTypeClassifier::DOUBLE)),
 ProcessorProperty("channels", BaseDataType(BaseDataTypeClassifier::DOUBLE)),
 ProcessorProperty("depth", BaseDataType(BaseDataTypeClassifier::DOUBLE))}) {}

DataBundle ImagePropertiesProcessor::execute(const DataBundle &dataMap, DataBundle &nodeSate) {
    Mat *image = dataMap.read<Mat*>("image");

    double height = image->size().height;
    double width = image->size().width;
    double channels = image->channels();
    double depth = image->depth();
    
    DataBundle resultDataBundle;
    resultDataBundle.write("height", height);
    resultDataBundle.write("width", width);
    resultDataBundle.write("channels", channels);
    resultDataBundle.write("depth", depth);
    prepareResult(&resultDataBundle);
    
    return resultDataBundle;
}
