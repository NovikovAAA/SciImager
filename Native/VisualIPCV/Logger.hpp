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

using namespace std;

class Logger {
private:
    Logger() {}
    Logger(const Logger&);
    Logger& operator=(Logger&);
public:
    static Logger& getInstance() {
        static Logger instance;
        return instance;;
    }
    void log(string info);
};

#endif /* Logger_hpp */
