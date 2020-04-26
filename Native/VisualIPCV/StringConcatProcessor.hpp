//
//  StringConcatProcessor.hpp
//  VisualIPCV
//
//  Created by Артём Новиков on 20.04.2020.
//

#ifndef StringConcatProcessor_hpp
#define StringConcatProcessor_hpp

#include <stdio.h>
#include <string>
#include "Processor.hpp"

class StringConcatProcessor : public Processor {
public:
    StringConcatProcessor();
    DataBundle execute(const DataBundle &dataMap, DataBundle &nodeSate) override;
private:
    std::string concat(std::string firstString, std::string secondString);
};

#endif /* StringConcatProcessor_hpp */
