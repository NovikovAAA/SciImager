//
//  CarNumbersSaver.hpp
//  ImageProcessors
//
//  Created by Артём Новиков on 11.06.2020.
//  Copyright © 2020 Артём Новиков. All rights reserved.
//

#ifndef CarNumbersSaver_hpp
#define CarNumbersSaver_hpp

#include <stdio.h>
#include <filesystem>
#include <opencv2/imgproc.hpp>
#include "Processor.hpp"

using namespace cv;
using namespace std::filesystem;

class IPCV_API CarNumbersSaver : public Processor {
public:
    CarNumbersSaver();
    DataBundle execute(const DataBundle &dataMap, DataBundle &nodeSate) override;
};

#endif /* CarNumbersSaver_hpp */
