//
//  CoincidenceChecker.hpp
//  VisualIPCV
//
//  Created by Артём Новиков on 15.06.2020.
//

#ifndef CoincidenceChecker_hpp
#define CoincidenceChecker_hpp

#include <stdio.h>
#include "Processor.hpp"

class IPCV_API CoincidenceChecker : public Processor {
public:
    CoincidenceChecker();
    DataBundle execute(const DataBundle &dataMap, DataBundle &nodeSate) override;
private:
    DataBundle handleException();
};

#endif /* CoincidenceChecker_hpp */
