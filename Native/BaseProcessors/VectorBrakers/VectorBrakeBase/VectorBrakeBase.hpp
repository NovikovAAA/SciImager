//
//  VectorBrakeBase.hpp
//  BaseProcessors
//
//  Created by Артём Новиков on 13.06.2020.
//  Copyright © 2020 Артём Новиков. All rights reserved.
//

#ifndef VectorBrakeBase_hpp
#define VectorBrakeBase_hpp

#include <stdio.h>
#include "Processor.hpp"

class VectorBrakeBase : public Processor {
public:
    VectorBrakeBase(std::string name, std::string module, std::string category, std::vector<ProcessorProperty> inputProperties, std::vector<ProcessorProperty> outputProperties);
    DataBundle execute(const DataBundle &dataMap, DataBundle &nodeSate) override;
};

#endif /* VectorBrakeBase_hpp */
