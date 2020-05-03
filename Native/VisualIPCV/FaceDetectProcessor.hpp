//
//  FaceDetectProcessor.hpp
//  VisualIPCV
//
//  Created by Артём Новиков on 03.05.2020.
//

#ifndef FaceDetectProcessor_hpp
#define FaceDetectProcessor_hpp

#include <stdio.h>

#include "Processor.hpp"

class IPCV_API FaceDetectProcessor : public Processor {
public:
    FaceDetectProcessor();
    DataBundle execute(const DataBundle &dataMap, DataBundle &nodeSate) override;
private:
//    std::string detect(std::string firstString, std::string secondString);
};

#endif /* FaceDetectProcessor_hpp */
