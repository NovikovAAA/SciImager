#include <opencv2/optflow.hpp>
#include <iostream>
#include <opencv2/core.hpp>
#include <opencv2/highgui.hpp>
#include <opencv2/imgproc.hpp>
#include <VisualIPCV/ProcessorManager.hpp>
#include <VisualIPCV/Logger.hpp>

using namespace cv;
using namespace std;

int main(int argc, const char * argv[]) {
    Logger& logger = Logger::getInstance();
    logger.log("test2");
    
//    Processor *sumProcessor = ProcessorManager::find("Core", "Sum");
//
//    DataBundle input;
//    input.write("a", 5.0);
//    input.write("b", 8.0);
//
//    DataBundle nodeState;
//
//    DataBundle result = sumProcessor->execute(input, nodeState);
//
//    cout << result.read<double>("result") << endl;
    
    return 0;
}

