//
//  MedianBlur.hpp
//  VisualIPCV
//
//  Created by Артём Новиков on 13.06.2020.
//

#ifndef MedianBlur_hpp
#define MedianBlur_hpp

#include <stdio.h>
#include "Processor.hpp"

class IPCV_API MedianBlurProcessor : public Processor {
public:
    MedianBlurProcessor();
    DataBundle execute(const DataBundle &dataMap, DataBundle &nodeSate) override;
};

#endif /* MedianBlur_hpp */
