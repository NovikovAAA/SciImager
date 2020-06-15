//
//  ImageCropProcessor.hpp
//  VisualIPCV
//
//  Created by Артём Новиков on 09.05.2020.
//

#ifndef ImageCropProcessor_hpp
#define ImageCropProcessor_hpp

#include <stdio.h>
#include "Processor.hpp"

class IPCV_API ImageCropProcessor : public Processor {
public:
    ImageCropProcessor();
    DataBundle execute(const DataBundle &dataMap, DataBundle &nodeSate) override;
};

#endif /* ImageCropProcessor_hpp */
