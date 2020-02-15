//
//  ProcessorProperty.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 15.02.2020.
//

#include "ProcessorProperty.hpp"

ProcessorProperty::ProcessorProperty() {
    name = "";
    type = DataTypeJ();
}

ProcessorProperty::ProcessorProperty(const ProcessorProperty & object) {
    this->name = object.name;
    this->type = DataTypeJ(object.type);
}

ProcessorProperty::ProcessorProperty(std::string name, DataTypeJ type) {
    this->name = name;
    this->type = DataTypeJ(type);
}
