//
//  Vector3Brake.hpp
//  BaseProcessors
//
//  Created by Артём Новиков on 12.06.2020.
//  Copyright © 2020 Артём Новиков. All rights reserved.
//

#ifndef Vector3Brake_hpp
#define Vector3Brake_hpp

#include <stdio.h>
#include "Processor.hpp"

class Vector3Brake : public Processor {
public:
    Vector3Brake();
    DataBundle execute(const DataBundle &dataMap, DataBundle &nodeSate) override;
};

#endif /* Vector3Brake_hpp */
