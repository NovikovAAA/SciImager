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
#include <array>
#include "BaseDataTypeClassifier.hpp"
#include "DllExport.h"

#define COLOR_ARRAY_SIZE 4

class BaseDataType {
public:
    IPCV_API BaseDataType();
    IPCV_API BaseDataType(const BaseDataType & object);
    IPCV_API BaseDataType(BaseDataTypeClassifier typeClassifier, std::array<double, COLOR_ARRAY_SIZE> color);
    
    IPCV_API void setTypeClassifier(BaseDataTypeClassifier typeClassifier);
    IPCV_API void setName(std::string name);
    IPCV_API void setColor(std::array<double, COLOR_ARRAY_SIZE> color);
    
    IPCV_API BaseDataTypeClassifier getTypeClassifier();
    IPCV_API std::string getName();
    IPCV_API std::array<double, COLOR_ARRAY_SIZE> getColor();
private:
    BaseDataTypeClassifier typeClassifier;
    std::string name;
    std::array<double, COLOR_ARRAY_SIZE> color;
    
    IPCV_API std::string typeNameForClassifier(BaseDataTypeClassifier typeClassifier);
};

#endif /* DataType_hpp */
