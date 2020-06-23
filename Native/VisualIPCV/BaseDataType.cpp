//
//  DataType.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 15.02.2020.
//

#include "BaseDataType.hpp"

BaseDataType::BaseDataType() {
    typeClassifier = BaseDataTypeClassifier::ANY;
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

BaseDataType::BaseDataType(BaseDataTypeClassifier typeClassifier) {
    this->typeClassifier = typeClassifier;
    this->name = typeNameForClassifier(typeClassifier);
    this->color = {0, 0, 0, 0};
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
        case BaseDataTypeClassifier::DOUBLE:
            return "Double";
        case BaseDataTypeClassifier::INTEGER:
            return "Integer";
        case BaseDataTypeClassifier::VECTOR2:
            return "Vector2";
        case BaseDataTypeClassifier::VECTOR3:
            return "Vector3";
        case BaseDataTypeClassifier::VECTOR4:
            return "Vector4";
        case BaseDataTypeClassifier::IMAGE:
            return "Image";
        case BaseDataTypeClassifier::BYTES:
            return "Bytes";
        case BaseDataTypeClassifier::STRING:
            return "String";
        case BaseDataTypeClassifier::FILE:
            return "File";
        case BaseDataTypeClassifier::DIRECTORY:
            return "Directory";
        default:
            return "Any";
    }
}
