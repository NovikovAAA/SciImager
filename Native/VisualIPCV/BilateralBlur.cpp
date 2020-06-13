//
//  Bilateral.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 13.06.2020.
//

#include "BilateralBlur.hpp"
#include "ProcessorManager.hpp"

#include <opencv2/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>

using namespace cv;

bool bilateralBlurProcessorProccessorLoadResult = ProcessorManager::registerProcessor(new BilateralBlur());

BilateralBlur::BilateralBlur() : Processor("BilateralBlur", "Core", "C++ Image Processors",
{ProcessorProperty("image", BaseDataType(BaseDataTypeClassifier::IMAGE)),
 ProcessorProperty("diametr", BaseDataType(BaseDataTypeClassifier::INTEGER)),
 ProcessorProperty("sigmaColor", BaseDataType(BaseDataTypeClassifier::DOUBLE)),
 ProcessorProperty("sigmaSpace", BaseDataType(BaseDataTypeClassifier::DOUBLE))},
{ProcessorProperty("result", BaseDataType(BaseDataTypeClassifier::IMAGE))}) {}

DataBundle BilateralBlur::execute(const DataBundle &dataMap, DataBundle &nodeSate) {
    Mat* image;
    try {
        image = dataMap.read<Mat *>(inputProperties[0].name);
    } catch (const std::exception& e) {
        return executionResult(outputProperties[0].name, new Mat());
    }
    
    int diametr = dataMap.read<int>(inputProperties[1].name);
    double sigmaColor = dataMap.read<double>(inputProperties[2].name);
    double sigmaSpace = dataMap.read<double>(inputProperties[3].name);
    
    diametr = diametr > 0 ? diametr : 1;
    
    Mat *dstImage = new Mat();
    bilateralFilter(*image, *dstImage, diametr, sigmaColor, sigmaSpace);
    return executionResult(outputProperties[0].name, dstImage);
}
