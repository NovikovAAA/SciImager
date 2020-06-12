//
//  BlurProcessor.hpp
//  VisualIPCV
//
//  Created by Артём Новиков on 13.06.2020.
//

#ifndef BlurProcessor_hpp
#define BlurProcessor_hpp

#include <stdio.h>
#include "Processor.hpp"

class IPCV_API AverageBlur : public Processor {
public:
    AverageBlur();
    DataBundle execute(const DataBundle &dataMap, DataBundle &nodeSate) override;
};

#endif /* BlurProcessor_hpp */
