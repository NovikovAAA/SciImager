//
//  DataType.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 15.02.2020.
//

#include "BaseDataType.hpp"

BaseDataType::BaseDataType() {
    name = "";
    color = {0, 0, 0, 0};
}

BaseDataType::BaseDataType(const BaseDataType & object) {
    name = object.name;
    for (int i = 0; i < COLOR_ARRAY_SIZE; i++) {
        this->color[i] = object.color[i];
    }
}

BaseDataType::BaseDataType(std::string name, std::array<double, COLOR_ARRAY_SIZE> color) {
    this->name = name;
    for (int i = 0; i < COLOR_ARRAY_SIZE; i++) {
        this->color[i] = color[i];
    }
}

void BaseDataType::setName(std::string name) {
    this->name = name;
}

void BaseDataType::setColor(std::array<double, COLOR_ARRAY_SIZE> color) {
    for (int i = 0; i < COLOR_ARRAY_SIZE; i++) {
        this->color[i] = color[i];
    }
}

std::string BaseDataType::getName() {
    return name;
}

std::array<double, COLOR_ARRAY_SIZE> BaseDataType::getColor() {
    return color;
}
