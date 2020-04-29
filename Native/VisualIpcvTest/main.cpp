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
    testBundle.write("a", string("test1"));

    DataBundle second = testBundle;
    string value = second.read<string>("a");
    second.write("a", string("privet"));
    testBundle = second;
    value = testBundle.read<string>("a");
    
    return 0;
}

