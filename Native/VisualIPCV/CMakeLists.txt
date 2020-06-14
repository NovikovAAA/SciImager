project(VisualIPCV)

find_package(OpenCV REQUIRED PATHS ${CMAKE_SOURCE_DIR}/../OpenCV/build/)

file(GLOB_RECURSE sources "*.h" "*.hpp" "*.cpp")
add_library(${PROJECT_NAME} SHARED ${sources})

target_compile_definitions(${PROJECT_NAME} PRIVATE -DIPCV_EXPORT)
target_include_directories(${PROJECT_NAME} PRIVATE ${OpenCV_INCLUDE_DIRS})
target_link_libraries(${PROJECT_NAME} PUBLIC ${OpenCV_LIBS})