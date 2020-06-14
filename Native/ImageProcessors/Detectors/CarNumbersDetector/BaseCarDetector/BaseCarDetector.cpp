//
//  BaseCarDetector.cpp
//  ImageProcessors
//
//  Created by Артём Новиков on 12.06.2020.
//  Copyright © 2020 Артём Новиков. All rights reserved.
//

#include "BaseCarDetector.hpp"
#include "BaseDetector.hpp"
#include <filesystem>

using namespace std::filesystem;

#pragma mark Constructors

BaseCarDetector::BaseCarDetector() {
    this->cascadePath = current_path().string() + "/Plugins/haarcascades/carNumberDetector.xml";
}

#pragma mark - Handlers

Mat *BaseCarDetector::obtainImageWithSelectedNumbers(Mat *sourceImage) {
    BaseDetector *detector = new BaseDetector(cascadePath);
    return detector->detectObjects(sourceImage);
}

Mat *BaseCarDetector::obtainImageWithSelectedNumbers(Mat *sourceImage, vector<double> colorsArray, double rectangleWidth) {
    BaseDetector *detector = new BaseDetector(cascadePath, colorsArray, rectangleWidth);
    return detector->detectObjects(sourceImage);
}

vector<Rect> BaseCarDetector::obtainNumbersRects(Mat *sourceImage) {
    BaseDetector *detector = new BaseDetector(cascadePath);
    vector<Rect> numbersRects = detector->detectObjectsRects(sourceImage);
    return numbersRects;;
}
