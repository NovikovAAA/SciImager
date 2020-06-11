//
//  CarNumbersDetector.hpp
//  ImageProcessors
//
//  Created by Артём Новиков on 11.06.2020.
//  Copyright © 2020 Артём Новиков. All rights reserved.
//

#ifndef CarNumbersDetector_hpp
#define CarNumbersDetector_hpp

#include <stdio.h>
#include "Processor.hpp"

class IPCV_API CarNumbersDetector : public Processor {
public:
    CarNumbersDetector();
    DataBundle execute(const DataBundle &dataMap, DataBundle &nodeSate) override;
};

#endif /* CarNumbersDetector_hpp */
