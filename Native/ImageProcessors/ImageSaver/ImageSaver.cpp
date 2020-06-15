//
//  ImageSaver.cpp
//  ImageProcessors
//
//  Created by Артём Новиков on 11.06.2020.
//  Copyright © 2020 Артём Новиков. All rights reserved.
//

#include "ImageSaver.hpp"
#include "Logger.hpp"

#include <opencv2/highgui.hpp>
#include <filesystem>
#include <ctime>

using namespace cv;
using namespace std;
using namespace std::filesystem;

#pragma mark - Constructors

ImageSaver::ImageSaver() {
    this->pathString = current_path().string() + "/";
}

ImageSaver::ImageSaver(string pathString) {
    if (!exists(pathString)) {
        this->pathString = current_path().string() + "/";
        Logger::getInstance().log("incorrect path. Images will be save in " + this->pathString);
    } else {
        this->pathString = pathString + "/";
    }
}

#pragma mark - Saving

void ImageSaver::save(Mat image) {
    time_t now = time(0);
    char* dt = ctime(&now);
    save(image, dt, "png");
}

void ImageSaver::save(Mat image, string imageName) {
    save(image, imageName, "png");
}

void ImageSaver::save(Mat image, string imageName, string extension) {
    path savingPath = pathString + imageName + "." + extension;
    imwrite(savingPath, image);
}
