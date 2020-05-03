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
{ProcessorProperty("image", BaseDataType(BaseDataTypeClassifier::IMAGE, {0, 0, 0, 0})), ProcessorProperty("cascadePath", BaseDataType(BaseDataTypeClassifier::STRING, {0, 0, 0, 0}))},
{ProcessorProperty("result", BaseDataType(BaseDataTypeClassifier::IMAGE, {0, 0, 0, 0}))}) {}

DataBundle FaceDetectProcessor::execute(const DataBundle &dataMap, DataBundle &nodeSate) {
    Mat image = dataMap.read<Mat>("image");
    std::string cascadePath = dataMap.read<std::string>("cascadePath");

    // Load Face cascade (.xml file)
    CascadeClassifier face_cascade;
    face_cascade.load(cascadePath);

    // Detect faces
    std::vector<Rect> faces;
    face_cascade.detectMultiScale(image, faces, 1.1, 2, 0 | CASCADE_SCALE_IMAGE, Size(30, 30));

    // Draw circles on the detected faces
    for (int i = 0; i < faces.size(); i++) {
        Point center(faces[i].x + faces[i].width * 0.5, faces[i].y + faces[i].height * 0.5);
        ellipse(image, center, Size(faces[i].width * 0.5, faces[i].height * 0.5), 0, 0, 360, Scalar(255, 0, 255), 4, 8, 0);
    }
    
    DataBundle resultDataBundle;
    resultDataBundle.write("result", image);
    prepareResult(&resultDataBundle);
    return resultDataBundle;
}
