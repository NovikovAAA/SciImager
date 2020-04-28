#include <iostream>
#include <opencv2/core.hpp>
#include <opencv2/highgui.hpp>
#include <opencv2/imgproc.hpp>
#include <VisualIPCV/ProcessorManager.hpp>
#include <VisualIPCV/Logger.hpp>

using namespace cv;
using namespace std;

int main(int argc, const char * argv[]) {
    DataBundle testBundle;
    testBundle.write("a", 20.0);
    
    DataBundle second = testBundle;
//    double value = second.read<double>("a");
//    testBundle.write("a", 30.0);
    testBundle = second;
//    value = testBundle.read<double>("a");
    
    return 0;
}

