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
        image = dataMap.read<Mat *>("image");
    } catch (const std::exception& e) {
        image = new Mat();

        DataBundle resultDataBundle;
        resultDataBundle.write("result", image);
        prepareResult(&resultDataBundle);
        return resultDataBundle;
    }
    int colorConversionCode = dataMap.read<int>("Color conversion code");
    colorConversionCode = colorConversionCode >= 0 ? colorConversionCode < 144 ? colorConversionCode : 143 : 0;
    
    Mat *dstImage = new Mat();
    
    try {
        cvtColor(*image, *dstImage, colorConversionCode);
    } catch (const std::exception& e) {
        DataBundle resultDataBundle;
        resultDataBundle.write("result", new Mat(*image));
        prepareResult(&resultDataBundle);
        return resultDataBundle;
    }
    
    DataBundle resultDataBundle;
    resultDataBundle.write("result", dstImage);
    prepareResult(&resultDataBundle);
    return resultDataBundle;
}
