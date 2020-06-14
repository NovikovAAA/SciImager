//
//  Bilateral.hpp
//  VisualIPCV
//
//  Created by Артём Новиков on 13.06.2020.
//

#ifndef Bilateral_hpp
#define Bilateral_hpp

#include <stdio.h>
#include "Processor.hpp"

class IPCV_API BilateralBlur : public Processor {
public:
    BilateralBlur();
    DataBundle execute(const DataBundle &dataMap, DataBundle &nodeSate) override;
};

#endif /* Bilateral_hpp */
