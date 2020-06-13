//
//  ThresholdProcessor.hpp
//  VisualIPCV
//
//  Created by Артём Новиков on 13.06.2020.
//

#ifndef ThresholdProcessor_hpp
#define ThresholdProcessor_hpp

#include <stdio.h>
#include "Processor.hpp"

class IPCV_API ThresholdProcessor : public Processor {
public:
    ThresholdProcessor();
    DataBundle execute(const DataBundle &dataMap, DataBundle &nodeSate) override;
};

#endif /* ThresholdProcessor_hpp */
