//
//  ResultTransferModel.h
//  VisualIPCV
//
//  Created by Артём Новиков on 13.06.2020.
//

#ifndef ResultTransferModel_h
#define ResultTransferModel_h

#include <string>

template <class T>
struct ResultTransferModel {
    std::string key;
    T const &value;
};

#endif /* ResultTransferModel_h */
