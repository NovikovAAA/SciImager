//
//  DistanceTransformProcessor.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 13.06.2020.
//

#include "DistanceTransformProcessor.hpp"
#include "ProcessorManager.hpp"

#include <opencv2/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>

using namespace cv;

bool distanceTransformProcessorLoadResult = ProcessorManager::registerProcessor(new DistanceTransformProcessor());

DistanceTransformProcessor::DistanceTransformProcessor() : Processor("DistanceTransform", "Core", "C++ Image Processors",
{ProcessorProperty("image", BaseDataType(BaseDataTypeClassifier::IMAGE)),
 ProcessorProperty("distance type", BaseDataType(BaseDataTypeClassifier::INTEGER))},
{ProcessorProperty("result", BaseDataType(BaseDataTypeClassifier::IMAGE))}) {}

DataBundle DistanceTransformProcessor::execute(const DataBundle &dataMap, DataBundle &nodeSate) {
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
    int distanceType = dataMap.read<int>("distance type");
    distanceType = distanceType < 0 ? 0 : distanceType > 7 ? 7 : distanceType;
    
    Mat *dstImage = new Mat();
    try {
        distanceTransform(*image, *dstImage, distanceType, 3);
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
