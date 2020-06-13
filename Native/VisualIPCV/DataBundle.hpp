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
#include <vector>
#include <string>
#include "DataValue.hpp"
#include "BaseDataType.hpp"

#include <any>

struct DataBundle {
    std::map<std::string, std::any> dataMap;
    std::map<std::string, BaseDataTypeClassifier> outputPropertiesDataTypes;
    
    DataBundle() {}
    template <class T>
    T read(std::string name) const {
        auto result = dataMap.find(name)->second;
        return std::any_cast<T>(result);
    }
    
    template <class T>
    void write(std::string name, T const &value) {
        dataMap[name] = value;
    }
};

#endif /* DataBundle_hpp */
