//
//  BaseFaceDetector.hpp
//  ImageProcessors
//
//  Created by Артём Новиков on 12.06.2020.
//  Copyright © 2020 Артём Новиков. All rights reserved.
//

#ifndef BaseFaceDetector_hpp
#define BaseFaceDetector_hpp

#include <stdio.h>
#include <vector>
#include <string>
#include <opencv2/core.hpp>

using namespace::std;
using namespace cv;

class BaseFaceDetector {
public:
    BaseFaceDetector(string cascadePath);
    
    Mat *obtainImageWithSelectedFaces(Mat *sourceImage);
    vector<Rect> obtainFacesRects(Mat *sourceImage);
private:
    string cascadePath;
};

#endif /* BaseFaceDetector_hpp */
