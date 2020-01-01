project(VisualIPCV)
include(ExternalProject)

file(GLOB sources "*.h" "*.hpp" "*.cpp")
add_library(${PROJECT_NAME} SHARED ${sources})

target_include_directories(${PROJECT_NAME} PRIVATE "${CMAKE_SOURCE_DIR}/OpenCV/include")
target_link_directories(${PROJECT_NAME} PRIVATE ${CMAKE_SOURCE_DIR}/OpenCV/x64/)
target_link_libraries(${PROJECT_NAME} PRIVATE opencv_world420d.lib)

ExternalProject_Add(OpenCV
	GIT_REPOSITORY "https://github.com/opencv/opencv.git"
	GIT_TAG "origin/4.2.0"
	CMAKE_ARGS -DBUILD_SHARED_LIBS=1
	PREFIX "${CMAKE_SOURCE_DIR}/OpenCV")