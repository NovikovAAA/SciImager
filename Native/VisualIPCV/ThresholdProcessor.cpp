//
//  ThresholdProcessor.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 13.06.2020.
//

#include "ThresholdProcessor.hpp"
#include "ProcessorManager.hpp"

#include <opencv2/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>

using namespace cv;

bool thresholdProcessorProccessorLoadResult = ProcessorManager::registerProcessor(new ThresholdProcessor());

ThresholdProcessor::ThresholdProcessor() : Processor("ThresholdProccessor", "Core", "C++ Image Processors",
{ProcessorProperty("image", BaseDataType(BaseDataTypeClassifier::IMAGE)),
 ProcessorProperty("tresh", BaseDataType(BaseDataTypeClassifier::DOUBLE)),
 ProcessorProperty("max value", BaseDataType(BaseDataTypeClassifier::DOUBLE)),
 ProcessorProperty("treshold type", BaseDataType(BaseDataTypeClassifier::INTEGER))},
{ProcessorProperty("result", BaseDataType(BaseDataTypeClassifier::IMAGE))}) {}

DataBundle ThresholdProcessor::execute(const DataBundle &dataMap, DataBundle &nodeSate) {
    Mat* image;
    try {
        image = dataMap.read<Mat *>(inputProperties[0].name);
    } catch (const std::exception& e) {
        return executionResult(outputProperties[0].name, new Mat());
    }
    double tresh = dataMap.read<double>(inputProperties[1].name);
    double maxValue = dataMap.read<double>(inputProperties[2].name);
    int tresholdType = dataMap.read<int>(inputProperties[3].name);
    
    tresh = tresh < 0 ? 0 : tresh > 255 ? 255 : tresh;
    maxValue = maxValue < 0 ? 0 : maxValue > 255 ? 255 : maxValue;
    tresholdType = tresholdType < 0 ? 0 : tresholdType > 8 ? 16 : tresholdType;
    
    Mat *dstImage = new Mat();
    try {
        threshold(*image, *dstImage, tresh, maxValue, tresholdType);
    } catch (const std::exception& e) {
        return executionResult(outputProperties[0].name, new Mat(*image));
    }
    return executionResult(outputProperties[0].name, dstImage);
}
