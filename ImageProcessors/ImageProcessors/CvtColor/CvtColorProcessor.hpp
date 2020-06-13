//
//  CvtColorProcessor.hpp
//  VisualIPCV
//
//  Created by Артём Новиков on 13.06.2020.
//

#ifndef CvtColorProcessor_hpp
#define CvtColorProcessor_hpp

#include <stdio.h>
#include "Processor.hpp"

class IPCV_API CvtColorProcessor : public Processor {
public:
    CvtColorProcessor();
    DataBundle execute(const DataBundle &dataMap, DataBundle &nodeSate) override;
};

#endif /* CvtColorProcessor_hpp */
