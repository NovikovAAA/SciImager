//
//  DataType.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 15.02.2020.
//

#include "BaseDataType.hpp"

BaseDataType::BaseDataType() {
    typeClassifier = ANY;
    name = "";
    color = {0, 0, 0, 0};
}

BaseDataType::BaseDataType(const BaseDataType & object) {
    typeClassifier = object.typeClassifier;
    name = object.name;
    for (int i = 0; i < COLOR_ARRAY_SIZE; i++) {
        this->color[i] = object.color[i];
    }
}

BaseDataType::BaseDataType(BaseDataTypeClassifier typeClassifier, std::array<double, COLOR_ARRAY_SIZE> color) {
    this->typeClassifier = typeClassifier;
    this->name = typeNameForClassifier(typeClassifier);
    for (int i = 0; i < COLOR_ARRAY_SIZE; i++) {
        this->color[i] = color[i];
    }
}

void BaseDataType::setTypeClassifier(BaseDataTypeClassifier typeClassifier) {
    this->typeClassifier = typeClassifier;
}

void BaseDataType::setName(std::string name) {
    this->name = name;
}

void BaseDataType::setColor(std::array<double, COLOR_ARRAY_SIZE> color) {
    for (int i = 0; i < COLOR_ARRAY_SIZE; i++) {
        this->color[i] = color[i];
    }
}

BaseDataTypeClassifier BaseDataType::getTypeClassifier() {
    return typeClassifier;
}

std::string BaseDataType::getName() {
    return name;
}

std::array<double, COLOR_ARRAY_SIZE> BaseDataType::getColor() {
    return color;
}

std::string BaseDataType::typeNameForClassifier(BaseDataTypeClassifier typeClassifier) {
    switch (typeClassifier) {
        case NUMBER:
            return "Number";
        case VECTOR2:
            return "Vector2";
        case VECTOR3:
            return "Vector3";
        case VECTOR4:
            return "Vector4";
        case IMAGE:
            return "Image";
        case BYTES:
            return "Bytes";
        case STRING:
            return "String";
        default:
            return "Any";;
    }
}
