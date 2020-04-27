//
//  Logger.hpp
//  VisualIPCV
//
//  Created by Артём Новиков on 12.04.2020.
//

#ifndef Logger_hpp
#define Logger_hpp

#include <stdio.h>
#include <string>
#include "DllExport.h"

using namespace std;

class Logger {
private:
    Logger() {}
    IPCV_API Logger(const Logger&);
    IPCV_API Logger& operator=(Logger&);
public:
    static Logger& getInstance() {
        static Logger instance;
        return instance;
    }
    IPCV_API void log(string info);
};

#endif /* Logger_hpp */
