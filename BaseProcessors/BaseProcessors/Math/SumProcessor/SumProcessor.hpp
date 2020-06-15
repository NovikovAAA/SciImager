//
//  SumProcessor.hpp
//  VisualIPCV
//
//  Created by Артём Новиков on 15.02.2020.
//

#ifndef SumProcessor_hpp
#define SumProcessor_hpp

#include <stdio.h>
#include "Processor.hpp"

class SumProcessor : public Processor {
public:
    SumProcessor();
    DataBundle execute(const DataBundle &dataMap, DataBundle &nodeSate) override;
private:
    double sum(double a, double b);
};

#endif /* SumProcessor_hpp */
