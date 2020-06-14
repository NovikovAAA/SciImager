project(BaseProcessors)

find_package(OpenCV REQUIRED PATHS ${CMAKE_SOURCE_DIR}/../OpenCV/build/)

file(GLOB_RECURSE sources "*.h" "*.hpp" "*.cpp")
add_library(${PROJECT_NAME} SHARED ${sources})
source_group(TREE ${CMAKE_CURRENT_SOURCE_DIR} FILES ${sources})

target_compile_definitions(${PROJECT_NAME} PRIVATE -DIPCV_EXPORT)
target_include_directories(${PROJECT_NAME} PRIVATE ${OpenCV_INCLUDE_DIRS})
target_include_directories(${PROJECT_NAME} PRIVATE ../VisualIPCV)

target_link_libraries(${PROJECT_NAME} PUBLIC ${OpenCV_LIBS})
target_link_libraries(${PROJECT_NAME} PUBLIC VisualIPCV)

FILE(GLOB_RECURSE children LIST_DIRECTORIES true "**/*")
SET(SUBDIRS "")
FOREACH(child ${children})
  IF(IS_DIRECTORY ${child})
    LIST(APPEND SUBDIRS ${child})
  ENDIF()
ENDFOREACH()

FOREACH(subdir ${SUBDIRS})
  target_include_directories(${PROJECT_NAME} PRIVATE ${subdir})
ENDFOREACH()