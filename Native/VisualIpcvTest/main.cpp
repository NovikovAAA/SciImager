#include <iostream>
#include <opencv2/core.hpp>
#include <opencv2/highgui.hpp>
#include <opencv2/imgproc.hpp>
#include <opencv2/objdetect/objdetect.hpp>
#include <VisualIPCV/ProcessorManager.hpp>
#include <VisualIPCV/Logger.hpp>

#include <VisualIPCV/FaceDetectProcessor.hpp>

using namespace cv;
using namespace std;

int main(int argc, const char * argv[]) {
//    DataBundle testBundle;
//    testBundle.write("a", string("test1"));
//
//    DataBundle second = testBundle;
//    string value = second.read<string>("a");
//    second.write("a", string("privet"));
//    testBundle = second;
//    value = testBundle.read<string>("a");
    
//    Mat src1;
//    src1 = imread("/Users/artemnovikov/Documents/Учеба/Диплом/Scilmager/Native/VisualIpcvTest/me.jpeg");
//    namedWindow("Original image");
//    imshow("Original image", src1);
//
//    Mat gray, edge, draw;
//    cvtColor(src1, gray, COLOR_BGR2GRAY);
//
//    Canny(gray, edge, 50, 150, 3);
//
//    edge.convertTo(draw, CV_8U);
//    namedWindow("image");
//    imshow("image", draw);
//
//    waitKey(0);
    
    
    /// Захват лица
    
    Mat image;
    image = imread("/Users/artemnovikov/Documents/Учеба/Диплом/Scilmager/Native/VisualIpcvTest/me.jpeg");
    string cascadePath = "/Users/artemnovikov/Documents/Учеба/Диплом/Scilmager/Native/VisualIpcvTest/haarcascade_frontalface_alt2.xml";
    namedWindow("window1", 1);
    imshow("window1", image);

    FaceDetectProcessor processor;

    DataBundle inputDataBundle;
    DataBundle nodeState;
    inputDataBundle.write("image", image);
    inputDataBundle.write("cascadePath", cascadePath);
    DataBundle outputDataBundle = processor.execute(inputDataBundle, nodeState);
    
    Mat resultImage = outputDataBundle.read<Mat>("result");
    imshow("Detected Face", resultImage);
    waitKey(0);
    
    return 0;
}

