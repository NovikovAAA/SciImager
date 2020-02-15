//
//  DataType.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 15.02.2020.
//

#include "DataTypeJ.hpp"

DataTypeJ::DataTypeJ() {
    name = "";
    color = {0, 0, 0, 0};
}

DataTypeJ::DataTypeJ(const DataTypeJ & object) {
    name = object.name;
    for (int i = 0; i < COLOR_ARRAY_SIZE; i++) {
        this->color[i] = object.color[i];
    }
}

DataTypeJ::DataTypeJ(std::string name, std::array<double, COLOR_ARRAY_SIZE> color) {
    this->name = name;
    for (int i = 0; i < COLOR_ARRAY_SIZE; i++) {
        this->color[i] = color[i];
    }
}

void DataTypeJ::setName(std::string name) {
    this->name = name;
}

void DataTypeJ::setColor(std::array<double, COLOR_ARRAY_SIZE> color) {
    for (int i = 0; i < COLOR_ARRAY_SIZE; i++) {
        this->color[i] = color[i];
    }
}

std::string DataTypeJ::getName() {
    return name;
}

std::array<double, COLOR_ARRAY_SIZE> DataTypeJ::getColor() {
    return color;
}
