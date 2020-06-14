//
//  Processor.hpp
//  VisualIPCV
//
//  Created by Артём Новиков on 15.02.2020.
//

#ifndef Processor_hpp
#define Processor_hpp

#include <stdio.h>
#include <vector>
#include "DataBundle.hpp"
#include "ProcessorProperty.hpp"
#include "ResultTransferModel.hpp"
#include "Logger.hpp"

class Processor {
public:
    IPCV_API Processor(const Processor & object);
    IPCV_API Processor(std::string name, std::string module, std::string category, std::vector<ProcessorProperty> inputProperties, std::vector<ProcessorProperty> outputProperties, bool isProperty = false);
    
    std::string name;
    std::string module;
    std::string category;
    
    std::vector<ProcessorProperty> inputProperties;
    std::vector<ProcessorProperty> outputProperties;
    
    bool isProperty;
    
    IPCV_API virtual DataBundle execute(DataBundle const &dataMap, DataBundle &nodeSate) = 0;
    IPCV_API virtual void preExecute(DataBundle nodeState) {}
    IPCV_API virtual void postExecute(DataBundle nodeState) {}
    IPCV_API virtual void onCreate(DataBundle nodeState) {}
    IPCV_API virtual void onDestroyed(DataBundle nodeState) {}
    
    IPCV_API void prepareResult(DataBundle *resultDataBundle);
    
protected:
    template <class T>
    DataBundle executionResult(std::vector<const ResultTransferModel<T>> models) {
        DataBundle resultDataBundle;
        for (auto& model : models) {
           resultDataBundle.write(model.key, model.value);
        }
        prepareResult(&resultDataBundle);
        return resultDataBundle;
    }
    
    template <class... Args>
    DataBundle executionResult(ResultTransferModel<Args>...models) {
        DataBundle resultDataBundle;
        bool res = (resultDataBundle.write(models.key, models.value) && ...);
        prepareResult(&resultDataBundle);
        return resultDataBundle;
    }
    
    template <class T>
    DataBundle executionResult(std::string key, T const &value) {
        DataBundle resultDataBundle;
        resultDataBundle.write(key, value);
        prepareResult(&resultDataBundle);
        return resultDataBundle;
    }
};

#endif /* Processor_hpp */

