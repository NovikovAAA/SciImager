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
    Mat *src = new Mat(imread("/Users/artemnovikov/Documents/Учеба/Диплом/Scilmager/VisualIpcvEditor/test2.png"));
    string path = "/Users/artemnovikov/Documents/Учеба/Диплом/Scilmager/Native/VisualIpcvTest/haarcascade_frontalface_alt2.xml";
    
    DataBundle input;
    input.write("image", src);
    input.write("cascadePath", path);
    
    DataBundle nodeState;
    
    FaceDetectProcessor processor;
    processor.execute(input, nodeState);
    
    return 0;
}

