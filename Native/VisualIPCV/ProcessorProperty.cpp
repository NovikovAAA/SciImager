//
//  ProcessorProperty.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 15.02.2020.
//

#include "ProcessorProperty.hpp"

ProcessorProperty::ProcessorProperty() {
    name = "";
    type = BaseDataType();
}

ProcessorProperty::ProcessorProperty(const ProcessorProperty & object) {
    this->name = object.name;
    this->type = BaseDataType(object.type);
}

ProcessorProperty::ProcessorProperty(std::string name, BaseDataType type) {
    this->name = name;
    this->type = BaseDataType(type);
}
