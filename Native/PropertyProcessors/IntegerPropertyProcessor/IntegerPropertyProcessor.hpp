//
//  IntegerPropertyProcessor.hpp
//  VisualIPCV
//
//  Created by Артём Новиков on 07.06.2020.
//

#ifndef IntegerPropertyProcessor_hpp
#define IntegerPropertyProcessor_hpp

#include <stdio.h>
#include "Processor.hpp"

class IPCV_API IntegerPropertyProcessor : public Processor {
public:
    IntegerPropertyProcessor();
    DataBundle execute(const DataBundle &dataMap, DataBundle &nodeSate) override;
};

#endif /* IntegerPropertyProcessor_hpp */
