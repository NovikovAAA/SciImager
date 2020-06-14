//
//  WatershedProcessor.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 13.06.2020.
//

#include "WatershedProcessor.hpp"
#include "ProcessorManager.hpp"

#include <opencv2/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>

using namespace cv;

bool watershedProcessorLoadResult = ProcessorManager::registerProcessor(new WatershedProcessor());

WatershedProcessor::WatershedProcessor() : Processor("Watershed", "Core", "C++ Image Processors",
{ProcessorProperty("source image", BaseDataType(BaseDataTypeClassifier::IMAGE)),
 ProcessorProperty("thresholded image", BaseDataType(BaseDataTypeClassifier::IMAGE))},
{ProcessorProperty("result", BaseDataType(BaseDataTypeClassifier::IMAGE)),
 ProcessorProperty("objects count", BaseDataType(BaseDataTypeClassifier::INTEGER))}) {}

DataBundle WatershedProcessor::execute(const DataBundle &dataMap, DataBundle &nodeSate) {
    Mat* sourceImage;
    Mat *thresholdedImage;
    try {
        sourceImage = dataMap.read<Mat *>(inputProperties[0].name);
        thresholdedImage = dataMap.read<Mat *>(inputProperties[1].name);
    } catch (const std::exception& e) {
        return handleException();
    }
    
    Mat image_8u;
    thresholdedImage->convertTo(image_8u, CV_8U);
    
    vector<vector<Point>> contours;
    
    try {
        findContours(image_8u, contours, RETR_EXTERNAL, CHAIN_APPROX_SIMPLE);
    } catch(const std::exception& e) {
        return handleException();
    }
    
    Mat markers = Mat::zeros(thresholdedImage->size(), CV_32SC1);
    for (int i = 0; i < contours.size(); i++) {
        drawContours(markers, contours, i, Scalar::all(i + 1), -1);
    }
    circle(markers, Point(5,5), 3, CV_RGB(255,255,255), -1);
    
    try {
        watershed(*sourceImage, markers);
    } catch (const std::exception& e) {
        return handleException();
    }
    
    vector<Vec3b> colors;
    for (int i = 0; i < contours.size(); i++)
    {
        int b = cv::theRNG().uniform(0, 255);
        int g = cv::theRNG().uniform(0, 255);
        int r = cv::theRNG().uniform(0, 255);
        colors.push_back(Vec3b((uchar)b, (uchar)g, (uchar)r));
    }

    Mat dst = Mat::zeros(markers.size(), CV_8UC3);
    for (int i = 0; i < markers.rows; i++) {
        for (int j = 0; j < markers.cols; j++) {
            int index = markers.at<int>(i,j);
            if (index > 0 && index <= contours.size())
                dst.at<Vec3b>(i,j) = colors[index-1];
            else
                dst.at<Vec3b>(i,j) = Vec3b(0,0,0);
        }
    }
    
    DataBundle resultDataBundle;
    resultDataBundle.write(outputProperties[0].name, new Mat(dst));
    resultDataBundle.write(outputProperties[1].name, (int)contours.size());
    prepareResult(&resultDataBundle);
    return resultDataBundle;
}

DataBundle WatershedProcessor::handleException() {
    Logger::getInstance().log("exception");
    DataBundle resultDataBundle;
    resultDataBundle.write(outputProperties[0].name, new Mat());
    resultDataBundle.write(outputProperties[1].name, 0);
    prepareResult(&resultDataBundle);
    return resultDataBundle;
}
