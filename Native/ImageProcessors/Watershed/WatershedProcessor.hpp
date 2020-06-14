//
//  WatershedProcessor.hpp
//  VisualIPCV
//
//  Created by Артём Новиков on 13.06.2020.
//

#ifndef WatershedProcessor_hpp
#define WatershedProcessor_hpp

#include <stdio.h>
#include "Processor.hpp"

class IPCV_API WatershedProcessor : public Processor {
public:
    WatershedProcessor();
    DataBundle execute(const DataBundle &dataMap, DataBundle &nodeSate) override;
private:
    DataBundle handleException();
};

#endif /* WatershedProcessor_hpp */
