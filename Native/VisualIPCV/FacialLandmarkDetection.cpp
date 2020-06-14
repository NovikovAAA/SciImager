//
//  FacialLandmarkDetection.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 14.06.2020.
//

#include "FacialLandmarkDetection.hpp"
#include "ProcessorManager.hpp"
#include "DrawLandmarks.hpp"

#include <opencv2/imgproc.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/objdetect/objdetect.hpp>
#include <opencv2/face.hpp>

#include <filesystem>

using namespace std::filesystem;
using namespace std;
using namespace cv;
using namespace cv::face;

bool facialLandmarkDetectionProccessorLoadResult = ProcessorManager::registerProcessor(new FacialLandmarkDetection());

FacialLandmarkDetection::FacialLandmarkDetection() : Processor("Facial landmark detection", "Core", "C++ Image Processors",
{ProcessorProperty("image", BaseDataType(BaseDataTypeClassifier::IMAGE))},
{ProcessorProperty("facial value", BaseDataType(BaseDataTypeClassifier::DOUBLE)),
 ProcessorProperty("result", BaseDataType(BaseDataTypeClassifier::IMAGE))}) {}

DataBundle FacialLandmarkDetection::execute(const DataBundle &dataMap, DataBundle &nodeSate) {
    Mat* image;
    try {
        image = dataMap.read<Mat *>(inputProperties[0].name);
    } catch (const std::exception& e) {
        return handleException();
    }
    string cascadePath = current_path().string() + "/Plugins/haarcascades/haarcascade_frontalface_alt2.xml";
    string lbfmodelPath = current_path().string() + "/Plugins/haarcascades/lbfmodel.yml";
    
    CascadeClassifier cascade;
    if (!cascade.load(cascadePath)) {
        Logger::getInstance().log("cascade error");
        return handleException();
    }
    Ptr<Facemark> facemark = FacemarkLBF::create();
    try {
        facemark->loadModel(lbfmodelPath);
    } catch (const std::exception& e) {
        Logger::getInstance().log("facemark error");
        return handleException();
    }
    
    Mat *frame = new Mat(*image);
    vector<Rect> faces;
    cascade.detectMultiScale(*frame, faces);
    
    vector< vector<Point2f> > landmarks;
    if (facemark->fit(*frame, faces, landmarks)) {
        for (size_t i = 0; i < faces.size(); i++) {
            rectangle(*frame, faces[i], Scalar(0, 255, 0), 3);
        }
        for (int i = 0; i < landmarks.size(); i++) {
            drawLandmarks(*frame, landmarks[i]);
            
            for (size_t j = 0; j < landmarks[i].size(); j++) {
                circle(*frame, Point(landmarks[i][j].x, landmarks[i][j].y), 1, Scalar(255, 0, 0), 2);
            }
            
            line(*frame, Point(landmarks[i][27].x, landmarks[i][27].y), Point(landmarks[i][8].x, landmarks[i][8].y), Scalar(0, 0, 255), 2);
            float XL = (landmarks[i][45].x + landmarks[i][42].x) / 2;
            float YL = (landmarks[i][45].y + landmarks[i][42].y) / 2;
            float XR = (landmarks[i][39].x + landmarks[i][36].x) / 2;
            float YR = (landmarks[i][39].y + landmarks[i][36].y) / 2;
            line(*frame, Point(XL, YL), Point(XR, YR), Scalar(0, 0, 255), 2);
            float DX = XR - XL;
            float DY = YR - YL;
            float L = sqrt(DX * DX + DY * DY);
            float X1 = (landmarks[i][27].x);
            float Y1 = (landmarks[i][27].y);
            float X2 = (landmarks[i][8].x);
            float Y2 = (landmarks[i][8].y);
            float DX1 = abs(X1 - X2);
            float DY1 = abs(Y1 - Y2);
            float L1 = sqrt(DX1 * DX1 + DY1 * DY1);
            float X0 = (XL + XR) / 2;
            float Y0 = (YL + YR) / 2;
            float sin_AL = DY / L;
            float cos_AL = DX / L;
            
            float X_User_0 = (landmarks[i][27].x - X0) / L;
            float Y_User_0 = -(landmarks[i][27].y - Y0) / L;
            float X_User27 = X_User_0 * cos_AL - Y_User_0 * sin_AL;
            float Y_User27 = X_User_0 * sin_AL + Y_User_0 * cos_AL;
            
            X_User_0 = (landmarks[i][30].x - X0) / L;
            Y_User_0 = -(landmarks[i][30].y - Y0) / L;
            float X_User30 = X_User_0 * cos_AL - Y_User_0 * sin_AL;
            float Y_User30 = X_User_0 * sin_AL + Y_User_0 * cos_AL;
            
            if (abs(X_User27 - X_User30) <= 0.1) {
                Logger::getInstance().log(to_string(L1 / L));
                return executionResult(ResultTransferModel<double>{ outputProperties[0].name, (L1 / L) },
                                       ResultTransferModel<Mat *>{ outputProperties[1].name, frame });
            } else {
                putText(*frame, "Incorrect", Point(landmarks[i][27].x, landmarks[i][27].y), 1, 2, Scalar(0, 0, 255), 2);
                return executionResult(ResultTransferModel<double>{ outputProperties[0].name, 1000 },
                                       ResultTransferModel<Mat *>{ outputProperties[1].name, frame });
            }
         }
    }
    
    Logger::getInstance().log("result");
    return handleException();
}

DataBundle FacialLandmarkDetection::handleException() {
    return executionResult(ResultTransferModel<double>{ outputProperties[0].name, 0 },
                           ResultTransferModel<Mat *>{ outputProperties[1].name, new Mat() });
}
