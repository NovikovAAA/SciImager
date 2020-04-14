//
//  DataType.hpp
//  VisualIPCV
//
//  Created by Артём Новиков on 15.02.2020.
//

#ifndef DataType_hpp
#define DataType_hpp

#include <stdio.h>
#include <string>
#include <array>

#define COLOR_ARRAY_SIZE 4

class BaseDataType {
public:
    BaseDataType();
    BaseDataType(const BaseDataType & object);
    BaseDataType(std::string name, std::array<double, COLOR_ARRAY_SIZE> color);
    
    void setName(std::string name);
    void setColor(std::array<double, COLOR_ARRAY_SIZE> color);
    
    std::string getName();
    std::array<double, COLOR_ARRAY_SIZE> getColor();
private:
    std::string name;
    std::array<double, COLOR_ARRAY_SIZE> color;
};

#endif /* DataType_hpp */
