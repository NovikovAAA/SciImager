//
//  DoublePropertyProcessor.hpp
//  VisualIPCV
//
//  Created by Артём Новиков on 07.06.2020.
//

#ifndef DoublePropertyProcessor_hpp
#define DoublePropertyProcessor_hpp

#include <stdio.h>
#include "Processor.hpp"

class IPCV_API DoublePropertyProcessor : public Processor {
public:
    DoublePropertyProcessor();
    DataBundle execute(const DataBundle &dataMap, DataBundle &nodeSate) override;
};

#endif /* DoublePropertyProcessor_hpp */
