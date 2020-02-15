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
    template <class T>
    T read(std::string name) const {
        DataValue dataValue = dataMap.find(name)->second;
        
        T value;
        dataValue.read(&value);
        return value;
    }
    
    template <class T>
    void write(std::string name, T const &value) {
        DataValue &dataValue = dataMap[name];
        dataValue.write(&value);
    }
};

#endif /* DataBundle_hpp */
