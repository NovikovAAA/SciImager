//
//  ImageResize.hpp
//  VisualIPCV
//
//  Created by Артём Новиков on 13.06.2020.
//

#ifndef ImageResize_hpp
#define ImageResize_hpp

#include <stdio.h>
#include "Processor.hpp"

class IPCV_API ImageResize : public Processor {
public:
    ImageResize();
    DataBundle execute(const DataBundle &dataMap, DataBundle &nodeSate) override;
};

#endif /* ImageResize_hpp */
