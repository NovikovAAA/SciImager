//
//  NormalizeProcessor.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 13.06.2020.
//

#include "NormalizeProcessor.hpp"
#include "ProcessorManager.hpp"

#include <opencv2/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>

#include "Logger.hpp"

using namespace cv;

bool NormalizeProcessorLoadResult = ProcessorManager::registerProcessor(new NormalizeProcessor());

NormalizeProcessor::NormalizeProcessor() : Processor("NormalizeMinMax", "Core", "C++ Image Processors",
{ProcessorProperty("image", BaseDataType(BaseDataTypeClassifier::IMAGE)),
 ProcessorProperty("alpha", BaseDataType(BaseDataTypeClassifier::DOUBLE)),
 ProcessorProperty("beta", BaseDataType(BaseDataTypeClassifier::DOUBLE))},
{ProcessorProperty("result", BaseDataType(BaseDataTypeClassifier::IMAGE))}) {}

DataBundle NormalizeProcessor::execute(const DataBundle &dataMap, DataBundle &nodeSate) {
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
    double alpha = dataMap.read<double>("alpha");
    double beta = dataMap.read<double>("beta");
    
    Mat *dstImage = new Mat();
    try {
        cv::normalize(*image, *dstImage, alpha, beta, cv::NORM_MINMAX);
    } catch (const std::exception& e) {
        Logger::getInstance().log("exception");
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
