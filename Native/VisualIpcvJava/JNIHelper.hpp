//
//  JNIHelper.hpp
//  VisualIpcvJava
//
//  Created by Артём Новиков on 01.03.2020.
//

#ifndef JNIHelper_hpp
#define JNIHelper_hpp

#include <stdio.h>
#include <VisualIPCV/ProcessorManager.hpp>
#include "jni.h"

class JNIHelper {
public:
    static Processor* processorFromJNI(JNIEnv* env, jobject uid);
    static jobject propertiesForJNI(JNIEnv* env, jobject uid, std::vector<ProcessorProperty> sourceProperties);
};

#endif /* JNIHelper_hpp */
