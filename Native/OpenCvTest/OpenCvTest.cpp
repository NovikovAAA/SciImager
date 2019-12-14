#include <iostream>
#include "VisualIPCV.h"
#include "opencv2/highgui.hpp"

int main()
{
	cv::namedWindow("Test");
	cv::Mat image = cv::imread("test.png", cv::IMREAD_COLOR);
	image = filterRed(image);
	cv::imshow("Test", image);
	int c = cv::waitKey();
	return 0;
}
