project(VisualIpcvTest)

file(GLOB sources "*.h" "*.hpp" "*.cpp")
add_executable(${PROJECT_NAME} ${sources})

target_compile_definitions(${PROJECT_NAME} PRIVATE -DIPCV_EXPORT)
target_link_libraries(${PROJECT_NAME} PRIVATE VisualIPCV)
target_include_directories(${PROJECT_NAME} PRIVATE ../)