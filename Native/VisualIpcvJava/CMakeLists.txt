project(VisualIpcvJava)

find_package(JNI)

file(GLOB_RECURSE sources "*.h" "*.hpp" "*.cpp")
source_group(TREE ${CMAKE_CURRENT_SOURCE_DIR} FILES ${sources})

add_library(${PROJECT_NAME} SHARED ${sources})

target_link_libraries(${PROJECT_NAME} PRIVATE ${JNI_LIBRARIES})
target_link_libraries(${PROJECT_NAME} PRIVATE VisualIPCV)

target_include_directories(${PROJECT_NAME} PRIVATE ../)
target_include_directories(${PROJECT_NAME} PRIVATE ${INTERFACE_INCLUDE_DIRECTORIES})
target_include_directories(${PROJECT_NAME} PRIVATE ${JNI_INCLUDE_DIRS})
