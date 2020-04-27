#include <iostream>
#include <opencv2/core.hpp>
#include <opencv2/highgui.hpp>
#include <opencv2/imgproc.hpp>
#include <VisualIPCV/ProcessorManager.hpp>
#include <VisualIPCV/Logger.hpp>

using namespace cv;
using namespace std;

int main(int argc, const char * argv[]) {
    Processor *stringProcessor = ProcessorManager::find("Core", "StringConcat");

    DataBundle input;
    input.write("firstString", string("test"));
    input.write("secondString", string("test2"));

    DataBundle nodeState;

    DataBundle result = stringProcessor->execute(input, nodeState);

    cout << result.read<string>("result") << endl;
    
    return 0;
}

