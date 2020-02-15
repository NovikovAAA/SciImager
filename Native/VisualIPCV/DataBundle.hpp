//
//  DataBundle.hpp
//  VisualIPCV
//
//  Created by Артём Новиков on 15.02.2020.
//

#ifndef DataBundle_hpp
#define DataBundle_hpp

#include <stdio.h>
#include <map>
#include <string>
#include "DataValue.hpp"

struct DataBundle {
    std::map<std::string, DataValue> dataMap;
};

#endif /* DataBundle_hpp */
