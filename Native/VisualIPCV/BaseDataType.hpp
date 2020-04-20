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
#include "BaseDataTypeClassifier.hpp"

#define COLOR_ARRAY_SIZE 4

class BaseDataType {
public:
    BaseDataType();
    BaseDataType(const BaseDataType & object);
    BaseDataType(BaseDataTypeClassifier typeClassifier, std::array<double, COLOR_ARRAY_SIZE> color);
    
    void setTypeClassifier(BaseDataTypeClassifier typeClassifier);
    void setName(std::string name);
    void setColor(std::array<double, COLOR_ARRAY_SIZE> color);
    
    BaseDataTypeClassifier getTypeClassifier();
    std::string getName();
    std::array<double, COLOR_ARRAY_SIZE> getColor();
private:
    BaseDataTypeClassifier typeClassifier;
    std::string name;
    std::array<double, COLOR_ARRAY_SIZE> color;
    
    std::string typeNameForClassifier(BaseDataTypeClassifier typeClassifier);
};

#endif /* DataType_hpp */
