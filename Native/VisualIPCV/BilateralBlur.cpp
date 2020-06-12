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
        image = dataMap.read<Mat *>("image");
    } catch (const std::exception& e) {
        image = new Mat();

        DataBundle resultDataBundle;
        resultDataBundle.write("result", image);
        prepareResult(&resultDataBundle);
        return resultDataBundle;
    }
    
    int diametr = dataMap.read<int>("diametr");
    double sigmaColor = dataMap.read<double>("sigmaColor");
    double sigmaSpace = dataMap.read<double>("sigmaSpace");
    
    diametr = diametr > 0 ? diametr : 1;
    
    Mat *dstImage = new Mat();
    bilateralFilter(*image, *dstImage, diametr, sigmaColor, sigmaSpace);
    
    DataBundle resultDataBundle;
    resultDataBundle.write("result", dstImage);
    prepareResult(&resultDataBundle);
    return resultDataBundle;
}
