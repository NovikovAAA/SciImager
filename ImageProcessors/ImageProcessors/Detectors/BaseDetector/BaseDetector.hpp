//
//  BaseDetector.hpp
//  ImageProcessors
//
//  Created by Артём Новиков on 11.06.2020.
//  Copyright © 2020 Артём Новиков. All rights reserved.
//

#ifndef BaseDetector_hpp
#define BaseDetector_hpp

#include <stdio.h>
#include <string>
#include <vector>
#include <opencv2/core.hpp>

using namespace::std;
using namespace cv;

class BaseDetector {
public:
    BaseDetector(string cascadePath);
    BaseDetector(string cascadePath, double rectangleWidth);
    BaseDetector(string cascadePath, vector<double> colorsVector);
    BaseDetector(string cascadePath, vector<double> colorsVector, double rectangleWidth);
    
    Mat *detectObjects(Mat *sourceImage);
    vector<Rect> detectObjectsRects(Mat *sourceImage);
private:
    BaseDetector();
    
    double rectangleWidth;
    string cascadePath;
    vector<double> colorsVector;
};

#endif /* BaseDetector_hpp */
