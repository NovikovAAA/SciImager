//
//  BaseDetector.cpp
//  ImageProcessors
//
//  Created by Артём Новиков on 11.06.2020.
//  Copyright © 2020 Артём Новиков. All rights reserved.
//

#include "BaseDetector.hpp"
#include <opencv2/imgproc.hpp>
#include <opencv2/objdetect/objdetect.hpp>

#pragma mark - Constructors

BaseDetector::BaseDetector(string cascadePath) {
    this->rectangleWidth = 3.0;
    this->colorsVector = { 255.0, 0.0, 255.0};
    this->cascadePath = cascadePath;
}

BaseDetector::BaseDetector(string cascadePath, double rectangleWidth) {
    this->rectangleWidth = rectangleWidth >= 0 ? rectangleWidth : 0.0;
    this->cascadePath = cascadePath;
    this->colorsVector = { 255.0, 0.0, 255.0};
}

BaseDetector::BaseDetector(string cascadePath, vector<double> colorsVector) {
    this->rectangleWidth = 3.0;
    this->cascadePath = cascadePath;
    this->colorsVector = colorsVector;
}

BaseDetector::BaseDetector(string cascadePath, vector<double> colorsVector, double rectangleWidth) {
    this->rectangleWidth = rectangleWidth > 0 ? rectangleWidth : 1.0;
    this->cascadePath = cascadePath;
    this->colorsVector = colorsVector;
}

#pragma mark - Detecting

Mat *BaseDetector::detectObjects(Mat *sourceImage) {
    double red = colorsVector[0] > 0 ? colorsVector[0] > 255 ? 255 : colorsVector[0] : 0;
    double green = colorsVector[1] > 0 ? colorsVector[1] > 255 ? 255 : colorsVector[1] : 0;
    double blue = colorsVector[2] > 0 ? colorsVector[2] > 255 ? 255 : colorsVector[2] : 0;
    
    vector<Rect> objects = detectObjectsRects(sourceImage);
    
    Mat *resultImage = new Mat(*sourceImage);
    for (int i = 0; i < objects.size(); i++) {
        rectangle(*resultImage, objects[i], Scalar(red, green, blue), rectangleWidth);
    }
    return new Mat(*resultImage);
}

vector<Rect> BaseDetector::detectObjectsRects(Mat *sourceImage) {
    CascadeClassifier cascade;
    if (cascadePath.length() > 0 && cascade.load(cascadePath)) {
        vector<Rect> objects;
        cascade.detectMultiScale(*sourceImage, objects, 1.1, 2, 0 | CASCADE_SCALE_IMAGE, Size(30, 30));
        return objects;
    }
    return vector<Rect>();
}
