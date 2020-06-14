//
//  ImageProperties.hpp
//  VisualIPCV
//
//  Created by Артём Новиков on 04.05.2020.
//

#ifndef ImageProperties_hpp
#define ImageProperties_hpp

#include <stdio.h>
#include "Processor.hpp"

class IPCV_API ImagePropertiesProcessor : public Processor {
public:
    ImagePropertiesProcessor();
    DataBundle execute(const DataBundle &dataMap, DataBundle &nodeSate) override;
};

#endif /* ImageProperties_hpp */
