//
//  ImagePropertyProcessor.hpp
//  VisualIPCV
//
//  Created by Артём Новиков on 07.06.2020.
//

#ifndef ImagePropertyProcessor_hpp
#define ImagePropertyProcessor_hpp

#include <stdio.h>
#include "Processor.hpp"

class IPCV_API ImageTestTransferProcessor : public Processor {
public:
    ImageTestTransferProcessor();
    DataBundle execute(const DataBundle &dataMap, DataBundle &nodeSate) override;
};

#endif /* ImagePropertyProcessor_hpp */
