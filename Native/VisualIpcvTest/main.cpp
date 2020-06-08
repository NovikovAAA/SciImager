#include <iostream>
#include <opencv2/core.hpp>
#include <opencv2/highgui.hpp>
#include <opencv2/imgproc.hpp>
#include <opencv2/objdetect/objdetect.hpp>
#include <VisualIPCV/ProcessorManager.hpp>
#include <VisualIPCV/Logger.hpp>

#include <VisualIPCV/FaceDetectProcessor.hpp>
#include <VisualIPCV/ImageCropProcessor.hpp>
#include <VisualIPCV/Vector2PropertyProcessor.hpp>

using namespace cv;
using namespace std;

int main(int argc, const char * argv[]) {
    double *inputArray = new double(2);
    inputArray[0] = 3;
    inputArray[1] = 14;
    
    DataBundle inputBundle;
    DataBundle nodeState;
    
    inputBundle.write("Value", inputArray);
    
    Vector2PropertyProcessor processor;
    DataBundle outputBundle = processor.execute(inputBundle, nodeState);
    
    double *outputArray = outputBundle.read<double*>("Result");
    for (int i = 0; i < 2; i++) {
        cout << outputArray[i] << endl;
    }
    
    return 0;
}

