//
//  StringPropertyProcessor.hpp
//  VisualIPCV
//
//  Created by Артём Новиков on 10.06.2020.
//

#ifndef StringPropertyProcessor_hpp
#define StringPropertyProcessor_hpp

#include <stdio.h>
#include "Processor.hpp"

class IPCV_API StringPropertyProcessor : public Processor {
public:
    StringPropertyProcessor();
    DataBundle execute(const DataBundle &dataMap, DataBundle &nodeSate) override;
};

#endif /* StringPropertyProcessor_hpp */
