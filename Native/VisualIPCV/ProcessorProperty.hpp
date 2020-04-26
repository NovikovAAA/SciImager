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
#include "BaseDataType.hpp"

class ProcessorProperty {
public:
    ProcessorProperty();
    ProcessorProperty(const ProcessorProperty & object);
    ProcessorProperty(std::string name, BaseDataType type);
    
    std::string name;
    BaseDataType type;
};

#endif /* ProcessorProperty_hpp */
