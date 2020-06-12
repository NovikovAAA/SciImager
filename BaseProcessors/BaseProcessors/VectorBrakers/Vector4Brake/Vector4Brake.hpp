//
//  Vector4Brake.hpp
//  BaseProcessors
//
//  Created by Артём Новиков on 12.06.2020.
//  Copyright © 2020 Артём Новиков. All rights reserved.
//

#ifndef Vector4Brake_hpp
#define Vector4Brake_hpp

#include <stdio.h>
#include "Processor.hpp"

class Vector4Brake : public Processor {
public:
    Vector4Brake();
    DataBundle execute(const DataBundle &dataMap, DataBundle &nodeSate) override;
};

#endif /* Vector4Brake_hpp */
