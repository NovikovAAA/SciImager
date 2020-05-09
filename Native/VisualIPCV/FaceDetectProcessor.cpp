//
//  FaceDetectProcessor.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 03.05.2020.
//

#include "FaceDetectProcessor.hpp"
#include "ProcessorManager.hpp"
#include <opencv2/core.hpp>
#include <opencv2/imgproc.hpp>
#include <opencv2/objdetect/objdetect.hpp>

using namespace cv;

bool faceDetectProccessorLoadResult = ProcessorManager::registerProcessor(new FaceDetectProcessor());

FaceDetectProcessor::FaceDetectProcessor() : Processor("FaceDetect", "Core", "TEST_C++",
{ProcessorProperty("image", BaseDataType(BaseDataTypeClassifier::IMAGE)), ProcessorProperty("cascadePath", BaseDataType(BaseDataTypeClassifier::STRING))},
{ProcessorProperty("result", BaseDataType(BaseDataTypeClassifier::IMAGE))}) {}

DataBundle FaceDetectProcessor::execute(const DataBundle &dataMap, DataBundle &nodeSate) {
    Mat *image = dataMap.read<Mat*>("image");
    std::string cascadePath = dataMap.read<std::string>("cascadePath");

    CascadeClassifier face_cascade;
    face_cascade.load(cascadePath);

    // Detect faces
    std::vector<Rect> faces;
    face_cascade.detectMultiScale(*image, faces, 1.1, 2, 0 | CASCADE_SCALE_IMAGE, Size(30, 30));

    // Draw rects on the detected faces
    for (int i = 0; i < faces.size(); i++) {
        rectangle(*image, faces[i], Scalar(255, 0, 255), 5);
    }
    
    DataBundle resultDataBundle;
    resultDataBundle.write("result", image);
    prepareResult(&resultDataBundle);
    return resultDataBundle;
}
