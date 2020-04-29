//
//  Logger.cpp
//  VisualIPCV
//
//  Created by Артём Новиков on 12.04.2020.
//

#include "Logger.hpp"
#include <iostream>
#include <fstream>

void Logger::log(string info) {
    std::cout << info << std::endl;
    ofstream fout("logs.txt", ios::app);
    fout << info << endl;
    fout.close();
}
