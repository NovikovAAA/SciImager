#include <opencv2/optflow.hpp>
#include <iostream>
#include <opencv2/core.hpp>
#include <opencv2/highgui.hpp>
#include <opencv2/imgproc.hpp>
#include <VisualIPCV/Processor.hpp>
#include <VisualIPCV/DataValue.hpp>

using namespace cv;
using namespace std;

int main(int argc, const char * argv[]) {
    DataValue value;
    
    int a = 5;
    value.write(&a);
    
    
    return 0;
}

