//
//  BaseFaceDetector.cpp
//  ImageProcessors
//
//  Created by Артём Новиков on 12.06.2020.
//  Copyright © 2020 Артём Новиков. All rights reserved.
//

#include "BaseFaceDetector.hpp"
#include "BaseDetector.hpp"
#include <filesystem>

using namespace std::filesystem;

#pragma mark Constructors

BaseFaceDetector::BaseFaceDetector(string cascadePath) {
    this->cascadePath = cascadePath;
}

#pragma mark - Handlers

Mat *BaseFaceDetector::obtainImageWithSelectedFaces(Mat *sourceImage) {
    BaseDetector *detector = new BaseDetector(cascadePath);
    return detector->detectObjects(sourceImage);
}

vector<Rect> BaseFaceDetector::obtainFacesRects(Mat *sourceImage) {
    BaseDetector *detector = new BaseDetector(cascadePath, 0);
    vector<Rect> numbersRects = detector->detectObjectsRects(sourceImage);
    return numbersRects;;
}
