//
//  DataBundleJNI.hpp
//  VisualIpcvJava
//
//  Created by Артём Новиков on 12.04.2020.
//

#ifndef DataBundleJNI_hpp
#define DataBundleJNI_hpp

#include <stdio.h>
#include "jni.h"

struct DataBundleJNIModel {
    jclass dataBundleClass;
    jmethodID dataBundleConstructor;
    jfieldID dataMapValuesFieldID;
    jmethodID dataBundleWriteMethod;
};

#endif /* DataBundleJNI_hpp */
