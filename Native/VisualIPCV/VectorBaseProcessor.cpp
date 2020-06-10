//
//  VectorBaseProcessor.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 09.06.2020.
//

#include "VectorBaseProcessor.hpp"

VectorBaseProcessor::VectorBaseProcessor(std::string name, std::string module, std::string category, std::vector<ProcessorProperty> inputProperties, std::vector<ProcessorProperty> outputProperties) : Processor(name, module, category, inputProperties, outputProperties, true) {}

DataBundle VectorBaseProcessor::execute(const DataBundle &dataMap, DataBundle &nodeSate) {
    std::vector<double> inputArray = dataMap.read<std::vector<double>>("Value");
    DataBundle resultDataBundle;
    resultDataBundle.write("Result", inputArray);
    prepareResult(&resultDataBundle);
    return resultDataBundle;
}
