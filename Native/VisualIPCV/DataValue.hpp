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
#include <functional>

enum class AccessType {
    Destroy,
    Read,
    Write,
    Move
};

template <class T>
struct Accessor {
    static void action(std::vector<char> *buffer, void *other, AccessType accessType) {
        switch (accessType) {
            case AccessType::Destroy:
                ((T*)(buffer->data()))->~T();
                break;
            case AccessType::Read:
                *(T*)other = *(T*)(buffer->data());
                break;
            case AccessType::Write:
                buffer->resize(sizeof(T));
                new((void*)buffer->data()) T(*(T*)other);
                break;
            case AccessType::Move:
                buffer->resize(sizeof(T));
                *(T*)(buffer->data()) = std::move(*(T*)other);
                break;
        }
    }
};

class DataValue {
    std::vector<char> m_data;
    void (*action)(std::vector<char> *buffer, void *other, AccessType accessType) = nullptr;
    
    void clearActionIfNeeded() {
        if (action != nullptr) {
            action(&m_data, nullptr, AccessType::Destroy);
            action = nullptr;
        }
    }
public:
    DataValue() {
        clearActionIfNeeded();
    }
    DataValue(const DataValue &obj) {
        action = obj.action;
        if (action != nullptr) {
            action(&m_data, (void *)obj.m_data.data(), AccessType::Write);
        }
    }
    
    DataValue& operator=(const DataValue &obj) {
        clearActionIfNeeded();
        action = obj.action;
        
        if (action != nullptr) {
            action(&m_data, (void *)obj.m_data.data(), AccessType::Write);
        }
    }
    
    template <class T>
    void read(T* data) const {
        if (action != nullptr) {
            action((std::vector<char> *)&m_data, data, AccessType::Read);
        }
    }
    
    template <class T>
    void write(T* data) {
        clearActionIfNeeded();
        action = &Accessor<typename std::remove_cv<T>::type>::action;
        action(&m_data, (void *)data, AccessType::Write);
    }
    
    ~DataValue() {
        clearActionIfNeeded();
    };
};

#endif /* DataValue_hpp */
