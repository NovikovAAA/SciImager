//
//  PrimitiveDataTypeClassifier.h
//  VisualIpcvJava
//
//  Created by Артём Новиков on 14.04.2020.
//

#ifndef PrimitiveDataTypeClassifier_h
#define PrimitiveDataTypeClassifier_h

enum PrimitiveTypeClassifier {
    JNI_DOUBLE,
    JNI_INTEGER,
    JNI_STRING,
    JNI_FILE,
    JNI_DIRECTORY,
    JNI_IMAGE,
    JNI_VECTOR2,
    JNI_VECTOR3,
    JNI_VECTOR4,
    UNKNOWN
};

#endif /* PrimitiveDataTypeClassifier_h */
