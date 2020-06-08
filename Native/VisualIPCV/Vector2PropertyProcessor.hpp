//
//  Vector2PropertyProcessor.hpp
//  VisualIPCV
//
//  Created by Артём Новиков on 07.06.2020.
//

#ifndef Vector2PropertyProcessor_hpp
#define Vector2PropertyProcessor_hpp

#include <stdio.h>
#include "Processor.hpp"

class IPCV_API Vector2PropertyProcessor : public Processor {
public:
    Vector2PropertyProcessor();
    DataBundle execute(const DataBundle &dataMap, DataBundle &nodeSate) override;
};

#endif /* Vector2PropertyProcessor_hpp */
