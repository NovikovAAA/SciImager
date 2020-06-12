//
//  GaussianProcessor.hpp
//  VisualIPCV
//
//  Created by Артём Новиков on 13.06.2020.
//

#ifndef GaussianProcessor_hpp
#define GaussianProcessor_hpp

#include <stdio.h>
#include "Processor.hpp"

class IPCV_API GaussianBlurProcessor : public Processor {
public:
    GaussianBlurProcessor();
    DataBundle execute(const DataBundle &dataMap, DataBundle &nodeSate) override;
};

#endif /* GaussianProcessor_hpp */
