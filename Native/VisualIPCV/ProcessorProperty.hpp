//
//  ProcessorProperty.hpp
//  VisualIPCV
//
//  Created by Артём Новиков on 15.02.2020.
//

#ifndef ProcessorProperty_hpp
#define ProcessorProperty_hpp

#include <stdio.h>
#include <string>
#include "DataTypeJ.hpp"

class ProcessorProperty {
public:
    ProcessorProperty();
    ProcessorProperty(const ProcessorProperty & object);
    ProcessorProperty(std::string name, DataTypeJ type);
    
    std::string name;
    DataTypeJ type;
};

#endif /* ProcessorProperty_hpp */
