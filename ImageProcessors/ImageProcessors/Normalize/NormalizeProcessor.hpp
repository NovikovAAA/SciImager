//
//  NormalizeProcessor.hpp
//  VisualIPCV
//
//  Created by Артём Новиков on 13.06.2020.
//

#ifndef NormalizeProcessor_hpp
#define NormalizeProcessor_hpp

#include <stdio.h>
#include "Processor.hpp"

class IPCV_API NormalizeProcessor : public Processor {
public:
    NormalizeProcessor();
    DataBundle execute(const DataBundle &dataMap, DataBundle &nodeSate) override;
};

#endif /* NormalizeProcessor_hpp */
