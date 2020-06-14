//
//  VectorBaseProcessor.hpp
//  VisualIPCV
//
//  Created by Артём Новиков on 09.06.2020.
//

#ifndef VectorBaseProcessor_hpp
#define VectorBaseProcessor_hpp

#include <stdio.h>
#include "Processor.hpp"

class IPCV_API VectorBaseProcessor : public Processor {
public:
    VectorBaseProcessor(std::string name, std::string module, std::string category, std::vector<ProcessorProperty> inputProperties, std::vector<ProcessorProperty> outputProperties);
    DataBundle execute(const DataBundle &dataMap, DataBundle &nodeSate) override;
};

#endif /* VectorBaseProcessor_hpp */
