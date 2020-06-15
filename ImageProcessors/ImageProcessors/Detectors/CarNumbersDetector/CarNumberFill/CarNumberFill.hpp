//
//  CarNumberFill.hpp
//  ImageProcessors
//
//  Created by Артём Новиков on 12.06.2020.
//  Copyright © 2020 Артём Новиков. All rights reserved.
//

#ifndef CarNumberFill_hpp
#define CarNumberFill_hpp

#include <stdio.h>
#include "Processor.hpp"

class IPCV_API CarNumberFill : public Processor {
public:
    CarNumberFill();
    DataBundle execute(const DataBundle &dataMap, DataBundle &nodeSate) override;
};

#endif /* CarNumberFill_hpp */
