//
//  Vector2Brake.hpp
//  BaseProcessors
//
//  Created by Артём Новиков on 12.06.2020.
//  Copyright © 2020 Артём Новиков. All rights reserved.
//

#ifndef Vector2Brake_hpp
#define Vector2Brake_hpp

#include <stdio.h>
#include "Processor.hpp"

class Vector2Brake : public Processor {
public:
    Vector2Brake();
    DataBundle execute(const DataBundle &dataMap, DataBundle &nodeSate) override;
};

#endif /* Vector2Brake_hpp */
