//
//  ImagePropertyProcessor.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 10.06.2020.
//

#include "ImagePropertyProcessor.hpp"
#include "ProcessorManager.hpp"
#include <opencv2/core.hpp>
#include <opencv2/imgcodecs.hpp>
#include <string>

using namespace cv;
using namespace std;

bool imagePropertyProcessorResult = ProcessorManager::registerProcessor(new ImagePropertyProcessor());

ImagePropertyProcessor::ImagePropertyProcessor() : Processor("ImageProperty", "Core", "C++ Properties",
{ProcessorProperty("Path", BaseDataType(BaseDataTypeClassifier::PATH))},
{ProcessorProperty("Result", BaseDataType(BaseDataTypeClassifier::IMAGE))}, true) {}

DataBundle ImagePropertyProcessor::execute(const DataBundle &dataMap, DataBundle &nodeSate) {
    string imagePathString = dataMap.read<string>(inputProperties[0].name);
    Mat image = imread(imagePathString);
    return executionResult(outputProperties[0].name, new Mat(image));
}
