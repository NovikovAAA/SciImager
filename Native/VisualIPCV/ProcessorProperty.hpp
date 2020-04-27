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
    IPCV_API ProcessorProperty();
    IPCV_API ProcessorProperty(const ProcessorProperty & object);
    IPCV_API ProcessorProperty(std::string name, BaseDataType type);
    
    std::string name;
    BaseDataType type;
};

#endif /* ProcessorProperty_hpp */
