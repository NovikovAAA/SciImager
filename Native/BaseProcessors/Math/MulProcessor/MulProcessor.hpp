//
//  MulProcessor.hpp
//  BaseProcessors
//
//  Created by Артём Новиков on 12.06.2020.
//  Copyright © 2020 Артём Новиков. All rights reserved.
//

#ifndef MulProcessor_hpp
#define MulProcessor_hpp

#include <stdio.h>
#include "Processor.hpp"

class MulProcessor : public Processor {
public:
    MulProcessor();
    DataBundle execute(const DataBundle &dataMap, DataBundle &nodeSate) override;
private:
    double mul(double a, double b);
};

#endif /* MulProcessor_hpp */
