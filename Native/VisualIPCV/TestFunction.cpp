#include "pch.h"
#include "TestFunction.h"

IPCV_API cv::Mat filterRed(const cv::Mat& src)
{
	cv::Mat dst;
	cv::Mat_<int> kernel(3, 3);
	cv::blur(src, dst, cv::Size(5, 5));
	return dst;
}

IPCV_API cv::Mat loadImage(const std::string& filename)
{
	return cv::imread(filename, cv::IMREAD_COLOR);
}