//
//  BaseCarDetector.hpp
//  ImageProcessors
//
//  Created by Артём Новиков on 12.06.2020.
//  Copyright © 2020 Артём Новиков. All rights reserved.
//

#ifndef BaseCarDetector_hpp
#define BaseCarDetector_hpp

#include <stdio.h>
#include <vector>
#include <string>
#include <opencv2/core.hpp>

using namespace::std;
using namespace cv;

class BaseCarDetector {
public:
    BaseCarDetector();
    
    Mat *obtainImageWithSelectedNumbers(Mat *sourceImage);
    Mat *obtainImageWithSelectedNumbers(Mat *sourceImage, vector<double> colorsArray, double rectangleWidth);
    vector<Rect> obtainNumbersRects(Mat *sourceImage);
    
private:
    string cascadePath;
};

#endif /* BaseCarDetector_hpp */
