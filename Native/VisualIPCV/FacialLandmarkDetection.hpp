//
//  FacialLandmarkDetection.hpp
//  VisualIPCV
//
//  Created by Артём Новиков on 14.06.2020.
//

#ifndef FacialLandmarkDetection_hpp
#define FacialLandmarkDetection_hpp

#include <stdio.h>
#include "Processor.hpp"

class IPCV_API FacialLandmarkDetection : public Processor {
public:
    FacialLandmarkDetection();
    DataBundle execute(const DataBundle &dataMap, DataBundle &nodeSate) override;
private:
    DataBundle handleException();
};

#endif /* FacialLandmarkDetection_hpp */
