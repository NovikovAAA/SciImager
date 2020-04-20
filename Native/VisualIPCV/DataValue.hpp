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
    std::function<void()> denit;
public:
    template <class T>
    void read(T* data) {
        assert(sizeof(T) == m_data.size());
        *data = *((T*)&m_data[0]);
    }
    
    template <class T>
    void write(T* data) {
        if (denit != nullptr) {
            denit();
        }
        
        m_data.resize(sizeof(T));
        *((typename std::remove_cv<T>::type*)&m_data[0]) = *data;
        denit = [this](){
            ((T*)(&m_data[0]))->~T();
        };
    }
    
    ~DataValue() {
       if (denit != nullptr) {
            denit();
       }
    };
};

#endif /* DataValue_hpp */
