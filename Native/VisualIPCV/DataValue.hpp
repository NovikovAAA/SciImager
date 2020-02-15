//
//  DataValue.hpp
//  VisualIPCV
//
//  Created by Артём Новиков on 15.02.2020.
//

#ifndef DataValue_hpp
#define DataValue_hpp

#include <stdio.h>
#include <vector>
#include <cassert> 

class DataValue {
    std::vector<char> m_data;
public:
    template <class T>
    void read(T* data) {
        assert(sizeof(T) == m_data.size());
        std::memcpy(data, &m_data[0], sizeof(T));
    }
    
    template <class T>
    void write(T* data) {
        m_data.resize(sizeof(T));
        std::memcpy(&m_data[0], data, sizeof(T));
    }
};

#endif /* DataValue_hpp */
