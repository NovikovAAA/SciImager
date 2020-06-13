//
//  CvtColorProcessor.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 13.06.2020.
//

#include "CvtColorProcessor.hpp"
#include "ProcessorManager.hpp"

#include <opencv2/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>

using namespace cv;

bool cvtColorProcessorProccessorLoadResult = ProcessorManager::registerProcessor(new CvtColorProcessor());

CvtColorProcessor::CvtColorProcessor() : Processor("CvtColor", "Core", "C++ Image Processors",
{ProcessorProperty("image", BaseDataType(BaseDataTypeClassifier::IMAGE)),
 ProcessorProperty("Color conversion code", BaseDataType(BaseDataTypeClassifier::INTEGER))},
{ProcessorProperty("result", BaseDataType(BaseDataTypeClassifier::IMAGE))}) {}

DataBundle CvtColorProcessor::execute(const DataBundle &dataMap, DataBundle &nodeSate) {
    Mat* image;
    try {
        image = dataMap.read<Mat *>(inputProperties[0].name);
    } catch (const std::exception& e) {
        return executionResult(outputProperties[0].name, new Mat());
    }
    
    int colorConversionCode = dataMap.read<int>(inputProperties[1].name);
    colorConversionCode = colorConversionCode >= 0 ? colorConversionCode < 144 ? colorConversionCode : 143 : 0;
    
    Mat *dstImage = new Mat();
    
    try {
        cvtColor(*image, *dstImage, colorConversionCode);
    } catch (const std::exception& e) {
        return executionResult(outputProperties[0].name, new Mat(*image));
    }
    return executionResult(outputProperties[0].name, dstImage);
}
