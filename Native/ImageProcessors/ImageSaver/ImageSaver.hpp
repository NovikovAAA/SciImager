//
//  ImageSaver.hpp
//  ImageProcessors
//
//  Created by Артём Новиков on 11.06.2020.
//  Copyright © 2020 Артём Новиков. All rights reserved.
//

#ifndef ImageSaver_hpp
#define ImageSaver_hpp

#include <stdio.h>
#include <string>
#include <opencv2/core.hpp>

class ImageSaver {
public:
    ImageSaver();
    ImageSaver(std::string pathString);
    
    void save(cv::Mat image, std::string imageName, std::string extension);
    void save(cv::Mat image, std::string imageName);
    void save(cv::Mat image);
private:
    std::string pathString;
};

#endif /* ImageSaver_hpp */
