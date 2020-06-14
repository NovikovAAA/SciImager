//
//  DistanceTransformProcessor.hpp
//  VisualIPCV
//
//  Created by Артём Новиков on 13.06.2020.
//

#ifndef DistanceTransformProcessor_hpp
#define DistanceTransformProcessor_hpp

#include <stdio.h>
#include "Processor.hpp"

class IPCV_API DistanceTransformProcessor : public Processor {
public:
    DistanceTransformProcessor();
    DataBundle execute(const DataBundle &dataMap, DataBundle &nodeSate) override;
};

#endif /* DistanceTransformProcessor_hpp */
