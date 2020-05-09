#include <iostream>
#include <opencv2/core.hpp>
#include <opencv2/highgui.hpp>
#include <opencv2/imgproc.hpp>
#include <opencv2/objdetect/objdetect.hpp>
#include <VisualIPCV/ProcessorManager.hpp>
#include <VisualIPCV/Logger.hpp>

#include <VisualIPCV/FaceDetectProcessor.hpp>
#include <VisualIPCV/ImageCropProcessor.hpp>

using namespace cv;
using namespace std;

int main(int argc, const char * argv[]) {
    Mat *src = new Mat(imread("/Users/artemnovikov/Documents/Учеба/Диплом/Scilmager/VisualIpcvEditor/test2.png"));
    string path = "/Users/artemnovikov/Documents/Учеба/Диплом/Scilmager/Native/VisualIpcvTest/haarcascade_frontalface_alt2.xml";
    
    DataBundle input;
    input.write("image", src);
    input.write("x", 20.0);
    input.write("y", 20.0);
    input.write("width", 400.0);
    input.write("height", 400.0);
//    input.write("cascadePath", path);
    
    DataBundle nodeState;
    
    ImageCropProcessor processor;
    DataBundle outputDataBundle = processor.execute(input, nodeState);
    
    Mat *result = outputDataBundle.read<Mat*>("result");

    namedWindow("window1", 1); imshow("window1", *result);
    waitKey(0);
    return 0;
}

