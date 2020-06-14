//
//  CarNumbersSaver.hpp
//  ImageProcessors
//
//  Created by Артём Новиков on 11.06.2020.
//  Copyright © 2020 Артём Новиков. All rights reserved.
//

#ifndef CarNumbersSaver_hpp
#define CarNumbersSaver_hpp

#include <stdio.h>
#include "Processor.hpp"

class IPCV_API CarNumbersSaver : public Processor {
public:
    CarNumbersSaver();
    DataBundle execute(const DataBundle &dataMap, DataBundle &nodeSate) override;
};

#endif /* CarNumbersSaver_hpp */
