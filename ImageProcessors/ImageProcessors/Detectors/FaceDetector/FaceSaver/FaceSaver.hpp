//
//  FaceDetectSaver.hpp
//  ImageProcessors
//
//  Created by Артём Новиков on 12.06.2020.
//  Copyright © 2020 Артём Новиков. All rights reserved.
//

#ifndef FaceDetectSaver_hpp
#define FaceDetectSaver_hpp

#include <stdio.h>
#include "Processor.hpp"

class IPCV_API FaceSaver : public Processor {
public:
    FaceSaver();
    DataBundle execute(const DataBundle &dataMap, DataBundle &nodeSate) override;
};

#endif /* FaceDetectSaver_hpp */
